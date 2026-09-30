package com.example.data.model

data class PlayerLevelInfo(
    val level: Int,
    val title: String,
    val minXp: Int,
    val maxXp: Int
)

object LevelSystem {
    val levels = listOf(
        PlayerLevelInfo(1, "Biology Beginner", 0, 300),
        PlayerLevelInfo(2, "Amino Acid Learner", 300, 700),
        PlayerLevelInfo(3, "Protein Explorer", 700, 1300),
        PlayerLevelInfo(4, "Protein Builder", 1300, 2100),
        PlayerLevelInfo(5, "Protein Scientist", 2100, 3200),
        PlayerLevelInfo(6, "Biology Master", 3200, 5000)
    )

    fun getLevelForXp(xp: Int): PlayerLevelInfo {
        for (lvl in levels) {
            if (xp < lvl.maxXp) return lvl
        }
        return levels.last()
    }

    fun getProgressInLevel(xp: Int): Float {
        val currentLvl = getLevelForXp(xp)
        val range = (currentLvl.maxXp - currentLvl.minXp).toFloat().coerceAtLeast(1f)
        val progress = (xp - currentLvl.minXp).toFloat() / range
        return progress.coerceIn(0f, 1f)
    }
}

data class UserProfile(
    val name: String = "Student",
    val xp: Int = 0,
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val lastPlayDateString: String = "",
    val soundEnabled: Boolean = true,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val proteinsBuilt: Int = 0,
    val easyProteinsBuilt: Int = 0,
    val mediumProteinsBuilt: Int = 0,
    val hardProteinsBuilt: Int = 0,
    val completedLessonIds: Set<Int> = emptySet(),
    val unlockedAchievementIds: Set<String> = emptySet(),
    val dailyChallengeCompletedDate: String = "",
    val masterExamPassed: Boolean = false,
    val masterExamHighScore: Int = 0
) {
    val levelInfo: PlayerLevelInfo
        get() = LevelSystem.getLevelForXp(xp)

    val accuracyPercent: Int
        get() = if (totalQuestionsAnswered > 0) {
            ((totalCorrectAnswers.toDouble() / totalQuestionsAnswered) * 100).toInt()
        } else {
            100
        }
}
