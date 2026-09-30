package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val topic: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val xp: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastPlayDateString: String,
    val soundEnabled: Boolean,
    val totalQuestionsAnswered: Int,
    val totalCorrectAnswers: Int,
    val proteinsBuilt: Int,
    val easyProteinsBuilt: Int,
    val mediumProteinsBuilt: Int,
    val hardProteinsBuilt: Int,
    val completedLessonIdsCsv: String,
    val unlockedAchievementIdsCsv: String,
    val dailyChallengeCompletedDate: String,
    val masterExamPassed: Boolean,
    val masterExamHighScore: Int
)
