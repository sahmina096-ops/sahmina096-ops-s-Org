package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelSystem
import com.example.data.model.UserProfile
import com.example.data.repository.ProteinRepository
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA

import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import com.google.firebase.auth.FirebaseUser

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    unresolvedMistakesCount: Int,
    firebaseUser: FirebaseUser?,
    firestoreSyncStatus: String?,
    onSignInGoogle: () -> Unit,
    onSignOutGoogle: () -> Unit,
    onSyncFirestore: () -> Unit,
    onContinueGame: () -> Unit,
    onLearn: () -> Unit,
    onChallenges: () -> Unit,
    onDailyChallenge: () -> Unit,
    onEncyclopedia: () -> Unit,
    onStructureLab: () -> Unit,
    onProgress: () -> Unit,
    onReviewMistakes: () -> Unit,
    onMasterExam: () -> Unit,
    onBioSearch: () -> Unit,
    onVeoAnimator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val today = ProteinRepository.getTodayDateString()
    val isDailyDone = userProfile.dailyChallengeCompletedDate == today

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Profile Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WELCOME, ${userProfile.name.uppercase()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BioBlue,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = userProfile.levelInfo.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Level Badge
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BioBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L${userProfile.levelInfo.level}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // XP Progress
                val progress = LevelSystem.getProgressInLevel(userProfile.xp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Current Level Progress",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${userProfile.xp} / ${userProfile.levelInfo.maxXp} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BioBlue
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = EmeraldBio,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickStatItem(icon = Icons.Default.LocalFireDepartment, tint = AmberBond, label = "Streak", value = "${userProfile.currentStreak} d")
                    QuickStatItem(icon = Icons.Default.CheckCircle, tint = EmeraldBio, label = "Accuracy", value = "${userProfile.accuracyPercent}%")
                    QuickStatItem(icon = Icons.Default.Science, tint = BioBlue, label = "Proteins", value = "${userProfile.proteinsBuilt}")
                    QuickStatItem(icon = Icons.Default.Book, tint = VioletDNA, label = "Lessons", value = "${userProfile.completedLessonIds.size}/8")
                }
            }
        }

        // Firebase Cloud Account & Firestore Sync
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (firebaseUser != null) EmeraldBio.copy(alpha = 0.08f) else BioBlue.copy(alpha = 0.06f)
            ),
            modifier = Modifier.fillMaxWidth().testTag("firebase_account_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (firebaseUser != null) EmeraldBio.copy(alpha = 0.2f) else BioBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (firebaseUser != null) Icons.Default.CloudDone else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (firebaseUser != null) EmeraldBio else BioBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (firebaseUser != null) (firebaseUser.displayName ?: "Google Account") else "Offline Study Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (firebaseUser != null) "Cloud Sync: Firestore Connected" else "Sign in to backup progress to Firestore",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                }

                if (firebaseUser != null) {
                    Button(
                        onClick = onSyncFirestore,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldBio),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onSignInGoogle,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                        modifier = Modifier.height(36.dp).testTag("google_signin_button")
                    ) {
                        Text("Sign In", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (firestoreSyncStatus != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = EmeraldBio.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = firestoreSyncStatus,
                    fontSize = 11.sp,
                    color = EmeraldBio,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // Daily Challenge Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDailyDone) EmeraldBio.copy(alpha = 0.12f) else AmberBond.copy(alpha = 0.15f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDailyChallenge() }
                .testTag("daily_challenge_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDailyDone) Icons.Default.CheckCircle else Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = if (isDailyDone) EmeraldBio else AmberBond,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isDailyDone) "Daily Challenge Completed" else "Daily Biology Challenge",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isDailyDone) "Streak protected! Come back tomorrow." else "Complete today's quiz for +100 bonus XP!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDailyDone) EmeraldBio else AmberBond
                ) {
                    Text(
                        text = if (isDailyDone) "DONE" else "+100 XP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Primary Game Navigation Grid
        Text(
            text = "Game Hub",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardActionCard(
                title = "Build Protein",
                subtitle = "Easy • Med • Hard",
                icon = Icons.Default.Build,
                accentColor = BioBlue,
                onClick = onContinueGame,
                modifier = Modifier.weight(1f),
                testTag = "dash_build_protein"
            )
            DashboardActionCard(
                title = "Learn Biology",
                subtitle = "8 Rich Chapters",
                icon = Icons.Default.Book,
                accentColor = EmeraldBio,
                onClick = onLearn,
                modifier = Modifier.weight(1f),
                testTag = "dash_learn_biology"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardActionCard(
                title = "Challenges",
                subtitle = "7 Game Modes",
                icon = Icons.Default.SportsEsports,
                accentColor = VioletDNA,
                onClick = onChallenges,
                modifier = Modifier.weight(1f),
                testTag = "dash_challenges"
            )
            DashboardActionCard(
                title = "Structure Lab",
                subtitle = "1° 2° 3° 4° Folding",
                icon = Icons.Default.Science,
                accentColor = BioCyan,
                onClick = onStructureLab,
                modifier = Modifier.weight(1f),
                testTag = "dash_structure_lab"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardActionCard(
                title = "Encyclopedia",
                subtitle = "All 20 Amino Acids",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                accentColor = AmberBond,
                onClick = onEncyclopedia,
                modifier = Modifier.weight(1f),
                testTag = "dash_encyclopedia"
            )
            DashboardActionCard(
                title = "Review Mistakes",
                subtitle = if (unresolvedMistakesCount > 0) "$unresolvedMistakesCount To Practice" else "All Clear!",
                icon = Icons.Default.Psychology,
                accentColor = if (unresolvedMistakesCount > 0) CrimsonAlert else EmeraldBio,
                onClick = onReviewMistakes,
                modifier = Modifier.weight(1f),
                testTag = "dash_review_mistakes"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardActionCard(
                title = "BioSearch AI",
                subtitle = "Google Search Data",
                icon = Icons.Default.Language,
                accentColor = BioBlue,
                onClick = onBioSearch,
                modifier = Modifier.weight(1f),
                testTag = "dash_biosearch"
            )
            DashboardActionCard(
                title = "Veo Animator",
                subtitle = "Animate Photos to Video",
                icon = Icons.Default.Movie,
                accentColor = VioletDNA,
                onClick = onVeoAnimator,
                modifier = Modifier.weight(1f),
                testTag = "dash_veo_animator"
            )
        }

        // Final Master Exam Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = VioletDNA.copy(alpha = 0.12f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMasterExam() }
                .testTag("dash_master_exam")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = VioletDNA,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Protein Master Exam",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (userProfile.masterExamPassed) "Passed! High Score: ${userProfile.masterExamHighScore}%" else "Comprehensive 12-question final exam",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Button(
                    onClick = onMasterExam,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletDNA),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text(text = if (userProfile.masterExamPassed) "Retake" else "Take Exam", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickStatItem(
    icon: ImageVector,
    tint: Color,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
private fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
        }
    }
}
