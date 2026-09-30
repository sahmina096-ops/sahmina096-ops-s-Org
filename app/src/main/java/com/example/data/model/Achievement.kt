package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null
)

object AchievementData {
    val initialAchievements: List<Achievement> = listOf(
        Achievement(
            id = "first_protein",
            title = "First Protein",
            description = "Build your very first protein in the Protein Builder.",
            iconEmoji = "🧪",
            xpReward = 100
        ),
        Achievement(
            id = "amino_explorer",
            title = "Amino Acid Explorer",
            description = "Explore and inspect amino acids in the Encyclopedia.",
            iconEmoji = "🔬",
            xpReward = 100
        ),
        Achievement(
            id = "perfect_round",
            title = "Perfect Round",
            description = "Complete a protein build or quiz without making any mistakes.",
            iconEmoji = "⭐",
            xpReward = 150
        ),
        Achievement(
            id = "no_hint_builder",
            title = "No-Hint Builder",
            description = "Complete a Medium or Hard protein without using any hints.",
            iconEmoji = "🧠",
            xpReward = 200
        ),
        Achievement(
            id = "speed_builder",
            title = "Speed Builder",
            description = "Complete a speed challenge or fast protein construction.",
            iconEmoji = "⚡",
            xpReward = 150
        ),
        Achievement(
            id = "protein_expert",
            title = "Protein Expert",
            description = "Successfully synthesize a Hard difficulty protein sequence.",
            iconEmoji = "🏆",
            xpReward = 300
        ),
        Achievement(
            id = "quiz_master",
            title = "Quiz Master",
            description = "Achieve a perfect 100% score on a biology quiz.",
            iconEmoji = "🎯",
            xpReward = 200
        ),
        Achievement(
            id = "master_exam_passed",
            title = "Biology Champion",
            description = "Pass the comprehensive Final Protein Master Exam.",
            iconEmoji = "👑",
            xpReward = 500
        )
    )
}
