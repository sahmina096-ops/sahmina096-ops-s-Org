package com.example.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.data.model.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private const val TAG = "FirebaseManager"

    val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.getApps(com.example.ProteinApplication.instance).isNotEmpty()
        } catch (_: Exception) {
            false
        }

    val currentUser: FirebaseUser?
        get() = try {
            FirebaseAuth.getInstance().currentUser
        } catch (e: Exception) {
            Log.w(TAG, "FirebaseAuth not initialized: ${e.message}")
            null
        }

    suspend fun signInWithGoogle(context: Context, webClientId: String = ""): Result<FirebaseUser?> {
        return try {
            val credentialManager = CredentialManager.create(context)
            val effectiveClientId = webClientId.ifEmpty {
                "66857129292-app.apps.googleusercontent.com"
            }

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(effectiveClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = response.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = FirebaseAuth.getInstance().signInWithCredential(authCredential).await()
                Result.success(authResult.user)
            } else {
                Result.failure(Exception("Unsupported credential type: ${credential.type}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Google Sign-In failed", e)
            Result.failure(e)
        }
    }

    fun signOut() {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
            Log.w(TAG, "Sign-out error", e)
        }
    }

    suspend fun syncProfileToFirestore(profile: UserProfile): Boolean {
        val user = currentUser ?: return false
        return try {
            val db = FirebaseFirestore.getInstance()
            val userDoc = db.collection("users").document(user.uid)
            val data = hashMapOf(
                "name" to profile.name,
                "email" to (user.email ?: ""),
                "photoUrl" to (user.photoUrl?.toString() ?: ""),
                "xp" to profile.xp,
                "level" to profile.levelInfo.level,
                "streak" to profile.currentStreak,
                "longestStreak" to profile.longestStreak,
                "proteinsBuilt" to profile.proteinsBuilt,
                "accuracyPercent" to profile.accuracyPercent,
                "completedLessonsCount" to profile.completedLessonIds.size,
                "completedLessonIds" to profile.completedLessonIds.toList(),
                "unlockedAchievements" to profile.unlockedAchievementIds.toList(),
                "dailyChallengeCompletedDate" to profile.dailyChallengeCompletedDate,
                "masterExamPassed" to profile.masterExamPassed,
                "masterExamHighScore" to profile.masterExamHighScore,
                "lastSyncedAt" to System.currentTimeMillis()
            )
            userDoc.set(data, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing to Firestore", e)
            false
        }
    }

    suspend fun loadProfileFromFirestore(): Map<String, Any>? {
        val user = currentUser ?: return null
        return try {
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("users").document(user.uid).get().await()
            if (doc.exists()) doc.data else null
        } catch (e: Exception) {
            Log.e(TAG, "Error loading from Firestore", e)
            null
        }
    }
}
