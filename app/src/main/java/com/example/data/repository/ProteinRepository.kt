package com.example.data.repository

import com.example.data.db.MistakeEntity
import com.example.data.db.ProteinDao
import com.example.data.db.UserProfileEntity
import com.example.data.model.MistakeItem
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProteinRepository(private val dao: ProteinDao) {

    val userProfile: Flow<UserProfile> = dao.getUserProfile().map { entity ->
        if (entity != null) {
            UserProfile(
                name = entity.name,
                xp = entity.xp,
                currentStreak = entity.currentStreak,
                longestStreak = entity.longestStreak,
                lastPlayDateString = entity.lastPlayDateString,
                soundEnabled = entity.soundEnabled,
                totalQuestionsAnswered = entity.totalQuestionsAnswered,
                totalCorrectAnswers = entity.totalCorrectAnswers,
                proteinsBuilt = entity.proteinsBuilt,
                easyProteinsBuilt = entity.easyProteinsBuilt,
                mediumProteinsBuilt = entity.mediumProteinsBuilt,
                hardProteinsBuilt = entity.hardProteinsBuilt,
                completedLessonIds = parseCsvToSetInt(entity.completedLessonIdsCsv),
                unlockedAchievementIds = parseCsvToSetString(entity.unlockedAchievementIdsCsv),
                dailyChallengeCompletedDate = entity.dailyChallengeCompletedDate,
                masterExamPassed = entity.masterExamPassed,
                masterExamHighScore = entity.masterExamHighScore
            )
        } else {
            // Default initial profile
            val today = getTodayDateString()
            UserProfile(lastPlayDateString = today)
        }
    }

    val unresolvedMistakes: Flow<List<MistakeItem>> = dao.getUnresolvedMistakes().map { list ->
        list.map {
            MistakeItem(
                id = it.id,
                question = it.question,
                userAnswer = it.userAnswer,
                correctAnswer = it.correctAnswer,
                explanation = it.explanation,
                topic = it.topic,
                timestamp = it.timestamp,
                isResolved = it.isResolved
            )
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        val entity = UserProfileEntity(
            id = 1,
            name = profile.name,
            xp = profile.xp,
            currentStreak = profile.currentStreak,
            longestStreak = profile.longestStreak,
            lastPlayDateString = profile.lastPlayDateString,
            soundEnabled = profile.soundEnabled,
            totalQuestionsAnswered = profile.totalQuestionsAnswered,
            totalCorrectAnswers = profile.totalCorrectAnswers,
            proteinsBuilt = profile.proteinsBuilt,
            easyProteinsBuilt = profile.easyProteinsBuilt,
            mediumProteinsBuilt = profile.mediumProteinsBuilt,
            hardProteinsBuilt = profile.hardProteinsBuilt,
            completedLessonIdsCsv = profile.completedLessonIds.joinToString(","),
            unlockedAchievementIdsCsv = profile.unlockedAchievementIds.joinToString(","),
            dailyChallengeCompletedDate = profile.dailyChallengeCompletedDate,
            masterExamPassed = profile.masterExamPassed,
            masterExamHighScore = profile.masterExamHighScore
        )
        dao.insertOrUpdateProfile(entity)
    }

    suspend fun recordMistake(question: String, userAnswer: String, correctAnswer: String, explanation: String, topic: String) {
        val entity = MistakeEntity(
            question = question,
            userAnswer = userAnswer,
            correctAnswer = correctAnswer,
            explanation = explanation,
            topic = topic
        )
        dao.insertMistake(entity)
    }

    suspend fun resolveMistake(id: Long) {
        dao.markMistakeResolved(id)
    }

    suspend fun resetAllData() {
        dao.clearAllMistakes()
        dao.clearUserProfile()
        val defaultProfile = UserProfile(lastPlayDateString = getTodayDateString())
        saveProfile(defaultProfile)
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(Date())
        }

        fun getYesterdayDateString(): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(cal.time)
        }

        private fun parseCsvToSetInt(csv: String): Set<Int> {
            if (csv.isBlank()) return emptySet()
            return csv.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
        }

        private fun parseCsvToSetString(csv: String): Set<String> {
            if (csv.isBlank()) return emptySet()
            return csv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        }
    }
}
