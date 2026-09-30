package com.example.viewmodel

import com.example.data.model.BuildingDifficulty
import com.example.data.model.Lesson

sealed interface Screen {
    data object Landing : Screen
    data object Dashboard : Screen
    data object DifficultySelect : Screen
    data class ProteinBuilder(val difficulty: BuildingDifficulty) : Screen
    data object LearnList : Screen
    data class LessonDetail(val lessonId: Int) : Screen
    data object Encyclopedia : Screen
    data object StructureLab : Screen
    data object ChallengesMenu : Screen
    data class ChallengeGame(val modeId: Int) : Screen // Modes 2 through 7
    data object ProgressStats : Screen
    data object Achievements : Screen
    data object MistakeReview : Screen
    data object MistakePractice : Screen
    data object MasterExam : Screen
    data object Settings : Screen
    data object BioSearch : Screen
    data class BioSearchWithQuery(val initialQuery: String) : Screen
    data object VeoAnimator : Screen
}

data class FeedbackData(
    val isCorrect: Boolean,
    val title: String,
    val message: String,
    val conceptTested: String,
    val explanation: String,
    val xpEarned: Int = 0,
    val onDismiss: () -> Unit = {}
)
