package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.db.ProteinDatabase
import com.example.data.model.AchievementData
import com.example.data.model.AminoAcidData
import com.example.data.model.BiologyTopic
import com.example.data.model.BuildingDifficulty
import com.example.data.model.LevelSystem
import com.example.data.model.MistakeItem
import com.example.data.model.ProteinRecipe
import com.example.data.model.ProteinRecipeData
import com.example.data.model.QuestionBank
import com.example.data.model.QuestionType
import com.example.data.model.QuizQuestion
import com.example.data.model.UserProfile
import com.example.data.repository.ProteinRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveBuildingState(
    val recipe: ProteinRecipe,
    val builtChain: List<String> = emptyList(),
    val optionPool: List<String> = emptyList(),
    val startTimeMs: Long = System.currentTimeMillis(),
    val mistakesInRound: Int = 0,
    val hintsUsed: Int = 0,
    val hintText: String? = null,
    val timeRemainingSec: Int? = null,
    val isCompleted: Boolean = false,
    val earnedXp: Int = 0,
    val justFormedBondIndex: Int? = null
)

data class ActiveChallengeState(
    val modeId: Int,
    val modeTitle: String,
    val currentQuestionIndex: Int = 0,
    val questions: List<QuizQuestion> = emptyList(),
    val score: Int = 0,
    val totalQuestions: Int = 5,
    val timeRemainingSec: Int? = null,
    val isFinished: Boolean = false,
    val feedback: FeedbackData? = null
)

data class MasterExamState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: MutableMap<Int, Int> = mutableMapOf(),
    val isCompleted: Boolean = false,
    val scorePercent: Int = 0,
    val correctCount: Int = 0,
    val totalCount: Int = 12,
    val topicsMastered: List<String> = emptyList(),
    val topicsToPractice: List<String> = emptyList(),
    val xpEarned: Int = 0
)

class ProteinViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ProteinDatabase.getDatabase(application)
    private val repository = ProteinRepository(db.proteinDao())

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())

    val unresolvedMistakes: StateFlow<List<MistakeItem>> = repository.unresolvedMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Screen navigation stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Landing))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Landing)
        .apply {
            viewModelScope.launch {
                _screenStack.collect { stack ->
                    value = stack.lastOrNull() ?: Screen.Landing
                }
            }
        }

    // Builder state
    private val _buildingState = MutableStateFlow<ActiveBuildingState?>(null)
    val buildingState: StateFlow<ActiveBuildingState?> = _buildingState.asStateFlow()

    // Active Challenge state
    private val _challengeState = MutableStateFlow<ActiveChallengeState?>(null)
    val challengeState: StateFlow<ActiveChallengeState?> = _challengeState.asStateFlow()

    // Master exam state
    private val _masterExamState = MutableStateFlow<MasterExamState?>(null)
    val masterExamState: StateFlow<MasterExamState?> = _masterExamState.asStateFlow()

    // Mistake practice state
    private val _currentPracticeIndex = MutableStateFlow(0)
    val currentPracticeIndex: StateFlow<Int> = _currentPracticeIndex.asStateFlow()

    // Global feedback dialog
    private val _activeFeedback = MutableStateFlow<FeedbackData?>(null)
    val activeFeedback: StateFlow<FeedbackData?> = _activeFeedback.asStateFlow()

    // Level-up / Achievement banner
    private val _celebrationNotice = MutableStateFlow<String?>(null)
    val celebrationNotice: StateFlow<String?> = _celebrationNotice.asStateFlow()

    private var timerJob: Job? = null

    init {
        checkDailyStreak()
    }

    private fun checkDailyStreak() {
        viewModelScope.launch {
            userProfile.collect { profile ->
                val today = ProteinRepository.getTodayDateString()
                val yesterday = ProteinRepository.getYesterdayDateString()

                if (profile.lastPlayDateString.isEmpty()) {
                    repository.saveProfile(profile.copy(lastPlayDateString = today, currentStreak = 1, longestStreak = 1))
                } else if (profile.lastPlayDateString != today) {
                    val newStreak = if (profile.lastPlayDateString == yesterday) {
                        profile.currentStreak + 1
                    } else {
                        1
                    }
                    val longest = maxOf(newStreak, profile.longestStreak)
                    repository.saveProfile(
                        profile.copy(
                            lastPlayDateString = today,
                            currentStreak = newStreak,
                            longestStreak = longest
                        )
                    )
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        SoundManager.playClick(userProfile.value.soundEnabled)
        val stack = _screenStack.value.toMutableList()
        stack.add(screen)
        _screenStack.value = stack
    }

    fun navigateBack(): Boolean {
        SoundManager.playClick(userProfile.value.soundEnabled)
        val stack = _screenStack.value.toMutableList()
        return if (stack.size > 1) {
            stack.removeAt(stack.lastIndex)
            _screenStack.value = stack
            true
        } else {
            false
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveProfile(userProfile.value.copy(soundEnabled = enabled))
        }
    }

    fun setStudentName(name: String) {
        viewModelScope.launch {
            repository.saveProfile(userProfile.value.copy(name = name.trim().ifEmpty { "Student" }))
        }
    }

    fun dismissFeedback() {
        SoundManager.playClick(userProfile.value.soundEnabled)
        val action = _activeFeedback.value?.onDismiss
        _activeFeedback.value = null
        action?.invoke()
    }

    fun dismissCelebration() {
        _celebrationNotice.value = null
    }

    // --- PHASE 1: PROTEIN BUILDER LOGIC ---

    fun startProteinBuilder(difficulty: BuildingDifficulty) {
        timerJob?.cancel()
        val recipes = ProteinRecipeData.recipes.filter { it.difficulty == difficulty }
        val recipe = recipes.random()

        // Generate pool of 6-8 options (all target residues + distractors)
        val neededResidues = recipe.sequence.distinct()
        val allCodes = AminoAcidData.allAminoAcids.map { it.code3 }
        val distractors = allCodes.filterNot { neededResidues.contains(it) }.shuffled()
        val pool = (neededResidues + distractors.take(8 - neededResidues.size)).shuffled()

        _buildingState.value = ActiveBuildingState(
            recipe = recipe,
            optionPool = pool,
            timeRemainingSec = difficulty.timeLimitSec
        )
        navigateTo(Screen.ProteinBuilder(difficulty))

        if (difficulty.timeLimitSec != null) {
            startBuilderTimer()
        }
    }

    private fun startBuilderTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _buildingState.value ?: break
                val remaining = current.timeRemainingSec ?: break
                if (remaining <= 1) {
                    _buildingState.value = current.copy(timeRemainingSec = 0)
                    handleBuilderTimeUp()
                    break
                } else {
                    _buildingState.value = current.copy(timeRemainingSec = remaining - 1)
                }
            }
        }
    }

    private fun handleBuilderTimeUp() {
        val current = _buildingState.value ?: return
        SoundManager.playIncorrect(userProfile.value.soundEnabled)
        _activeFeedback.value = FeedbackData(
            isCorrect = false,
            title = "Time's Up!",
            message = "The reaction timed out! Protein synthesis requires rapid ribosomal assembly.",
            conceptTested = "Reaction Kinetics",
            explanation = "Under cellular conditions, amino acids are assembled quickly. The target sequence was: ${current.recipe.sequence.joinToString(" - ")}",
            onDismiss = { navigateBack() }
        )
    }

    fun selectAminoAcidForChain(code3: String) {
        val current = _buildingState.value ?: return
        if (current.isCompleted) return

        val nextIndex = current.builtChain.size
        val expectedCode = current.recipe.sequence[nextIndex]

        if (code3 == expectedCode) {
            // Correct peptide bond formed!
            SoundManager.playCorrect(userProfile.value.soundEnabled)
            val updatedChain = current.builtChain + code3
            val isNowComplete = updatedChain.size == current.recipe.sequence.size

            _buildingState.value = current.copy(
                builtChain = updatedChain,
                justFormedBondIndex = if (updatedChain.size > 1) updatedChain.size - 2 else null
            )

            if (isNowComplete) {
                completeProteinBuild()
            }
        } else {
            // Mistake
            SoundManager.playIncorrect(userProfile.value.soundEnabled)
            val newMistakes = current.mistakesInRound + 1
            _buildingState.value = current.copy(mistakesInRound = newMistakes)

            val fullExpected = AminoAcidData.allAminoAcids.find { it.code3 == expectedCode }
            val fullSelected = AminoAcidData.allAminoAcids.find { it.code3 == code3 }

            viewModelScope.launch {
                repository.recordMistake(
                    question = "Next residue in ${current.recipe.name} at position ${nextIndex + 1}",
                    userAnswer = "${fullSelected?.name ?: code3} ($code3)",
                    correctAnswer = "${fullExpected?.name ?: expectedCode} ($expectedCode)",
                    explanation = "${fullExpected?.name ?: expectedCode} is required here. ${fullExpected?.description ?: ""}",
                    topic = "Protein Synthesis & Sequence"
                )
            }

            _activeFeedback.value = FeedbackData(
                isCorrect = false,
                title = "Bond Mismatch!",
                message = "Selected $code3 (${fullSelected?.name}), but position ${nextIndex + 1} requires $expectedCode (${fullExpected?.name}).",
                conceptTested = "Primary Sequence Fidelity",
                explanation = "In living cells, an incorrect amino acid causes structural misfolding or nonsense mutations. Look for $expectedCode: ${fullExpected?.sideChain}."
            )
        }
    }

    fun useBuilderHint() {
        val current = _buildingState.value ?: return
        if (current.isCompleted) return
        val nextIndex = current.builtChain.size
        val expected = current.recipe.sequence.getOrNull(nextIndex) ?: return
        val aa = AminoAcidData.allAminoAcids.find { it.code3 == expected }

        SoundManager.playClick(userProfile.value.soundEnabled)
        val hint = "Hint for position ${nextIndex + 1}: ${aa?.name} (${aa?.code3}) - ${aa?.sideChain} [${aa?.category?.displayName}]."
        _buildingState.value = current.copy(
            hintsUsed = current.hintsUsed + 1,
            hintText = hint
        )
    }

    private fun completeProteinBuild() {
        timerJob?.cancel()
        val current = _buildingState.value ?: return
        val profile = userProfile.value

        val elapsedSec = ((System.currentTimeMillis() - current.startTimeMs) / 1000).toInt()
        var xp = current.recipe.difficulty.basePoints

        // Bonuses
        val fastBonus = if (elapsedSec < 20) 50 else 0
        val noHintBonus = if (current.hintsUsed == 0) 50 else 0
        val perfectBonus = if (current.mistakesInRound == 0) 100 else 0
        xp += fastBonus + noHintBonus + perfectBonus

        _buildingState.value = current.copy(isCompleted = true, earnedXp = xp)
        SoundManager.playProteinComplete(profile.soundEnabled)

        // Update profile
        viewModelScope.launch {
            val easyCount = profile.easyProteinsBuilt + if (current.recipe.difficulty == BuildingDifficulty.EASY) 1 else 0
            val medCount = profile.mediumProteinsBuilt + if (current.recipe.difficulty == BuildingDifficulty.MEDIUM) 1 else 0
            val hardCount = profile.hardProteinsBuilt + if (current.recipe.difficulty == BuildingDifficulty.HARD) 1 else 0

            val updatedAchievements = profile.unlockedAchievementIds.toMutableSet()
            if (!updatedAchievements.contains("first_protein")) {
                updatedAchievements.add("first_protein")
                notifyAchievement("First Protein Built! +100 XP")
            }
            if (current.mistakesInRound == 0 && !updatedAchievements.contains("perfect_round")) {
                updatedAchievements.add("perfect_round")
                notifyAchievement("Perfect Round! +150 XP")
            }
            if (current.hintsUsed == 0 && current.recipe.difficulty != BuildingDifficulty.EASY && !updatedAchievements.contains("no_hint_builder")) {
                updatedAchievements.add("no_hint_builder")
                notifyAchievement("No-Hint Builder! +200 XP")
            }
            if (current.recipe.difficulty == BuildingDifficulty.HARD && !updatedAchievements.contains("protein_expert")) {
                updatedAchievements.add("protein_expert")
                notifyAchievement("Protein Expert Unlocked! +300 XP")
            }
            if (fastBonus > 0 && !updatedAchievements.contains("speed_builder")) {
                updatedAchievements.add("speed_builder")
                notifyAchievement("Speed Builder! +150 XP")
            }

            addXpAndSave(
                profile.copy(
                    proteinsBuilt = profile.proteinsBuilt + 1,
                    easyProteinsBuilt = easyCount,
                    mediumProteinsBuilt = medCount,
                    hardProteinsBuilt = hardCount,
                    totalQuestionsAnswered = profile.totalQuestionsAnswered + current.recipe.sequence.size + current.mistakesInRound,
                    totalCorrectAnswers = profile.totalCorrectAnswers + current.recipe.sequence.size,
                    unlockedAchievementIds = updatedAchievements
                ),
                xp
            )
        }
    }

    // --- PHASE 2: LESSON SYSTEM ---

    fun completeLessonQuiz(lessonId: Int, chosenIndex: Int) {
        val lesson = com.example.data.model.LessonData.lessons.find { it.id == lessonId } ?: return
        val profile = userProfile.value
        val isCorrect = chosenIndex == lesson.miniQuiz.correctIndex

        if (isCorrect) {
            SoundManager.playCorrect(profile.soundEnabled)
            viewModelScope.launch {
                val updatedLessons = profile.completedLessonIds + lessonId
                val updatedAchievements = profile.unlockedAchievementIds.toMutableSet()
                if (updatedLessons.size >= 8 && !updatedAchievements.contains("amino_explorer")) {
                    updatedAchievements.add("amino_explorer")
                    notifyAchievement("Master of Lessons! +100 XP")
                }
                addXpAndSave(
                    profile.copy(
                        completedLessonIds = updatedLessons,
                        unlockedAchievementIds = updatedAchievements,
                        totalQuestionsAnswered = profile.totalQuestionsAnswered + 1,
                        totalCorrectAnswers = profile.totalCorrectAnswers + 1
                    ),
                    75
                )
            }
            _activeFeedback.value = FeedbackData(
                isCorrect = true,
                title = "Excellent! +75 XP",
                message = "You mastered this chapter concept!",
                conceptTested = lesson.title,
                explanation = lesson.miniQuiz.explanation,
                xpEarned = 75,
                onDismiss = { navigateBack() }
            )
        } else {
            SoundManager.playIncorrect(profile.soundEnabled)
            viewModelScope.launch {
                repository.recordMistake(
                    question = lesson.miniQuiz.question,
                    userAnswer = lesson.miniQuiz.options.getOrElse(chosenIndex) { "Option $chosenIndex" },
                    correctAnswer = lesson.miniQuiz.options[lesson.miniQuiz.correctIndex],
                    explanation = lesson.miniQuiz.explanation,
                    topic = lesson.title
                )
                repository.saveProfile(
                    profile.copy(
                        totalQuestionsAnswered = profile.totalQuestionsAnswered + 1
                    )
                )
            }
            _activeFeedback.value = FeedbackData(
                isCorrect = false,
                title = "Not Quite",
                message = "Review the lesson materials and try again.",
                conceptTested = lesson.title,
                explanation = lesson.miniQuiz.explanation
            )
        }
    }

    // --- PHASE 3 & 4: GAME MODES & QUIZZES ---

    fun startChallengeMode(modeId: Int) {
        timerJob?.cancel()
        val allQuestions = QuestionBank.questions.shuffled()

        val modeQuestions = when (modeId) {
            2 -> allQuestions.filter { it.topic == BiologyTopic.AMINO_ACID_PROPERTIES }.take(5)
            3 -> allQuestions.filter { it.topic == BiologyTopic.PEPTIDE_BONDS }.take(5)
            4 -> allQuestions.filter { it.topic == BiologyTopic.PROTEIN_STRUCTURE }.take(5)
            5 -> allQuestions.take(5)
            6 -> allQuestions.shuffled().take(8) // Speed challenge
            7 -> allQuestions.filter { it.topic == BiologyTopic.PROTEIN_FUNCTIONS }.take(5)
            else -> allQuestions.take(5)
        }

        val modeTitles = mapOf(
            2 to "Identify the Amino Acid",
            3 to "Peptide Bond Challenge",
            4 to "Protein Structure Challenge",
            5 to "Biology Quiz",
            6 to "Rapid Speed Challenge (60s)",
            7 to "Mystery Protein Investigation"
        )

        _challengeState.value = ActiveChallengeState(
            modeId = modeId,
            modeTitle = modeTitles[modeId] ?: "Challenge",
            questions = modeQuestions,
            totalQuestions = modeQuestions.size,
            timeRemainingSec = if (modeId == 6) 60 else null
        )

        navigateTo(Screen.ChallengeGame(modeId))

        if (modeId == 6) {
            startSpeedTimer()
        }
    }

    private fun startSpeedTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _challengeState.value ?: break
                val remaining = state.timeRemainingSec ?: break
                if (remaining <= 1) {
                    _challengeState.value = state.copy(timeRemainingSec = 0, isFinished = true)
                    SoundManager.playLevelUp(userProfile.value.soundEnabled)
                    break
                } else {
                    _challengeState.value = state.copy(timeRemainingSec = remaining - 1)
                }
            }
        }
    }

    fun submitChallengeAnswer(optionIndex: Int) {
        val state = _challengeState.value ?: return
        if (state.isFinished) return

        val q = state.questions[state.currentQuestionIndex]
        val isCorrect = optionIndex == q.correctIndex
        val profile = userProfile.value

        if (isCorrect) {
            SoundManager.playCorrect(profile.soundEnabled)
            val newScore = state.score + 1
            val isLast = state.currentQuestionIndex + 1 >= state.totalQuestions

            _activeFeedback.value = FeedbackData(
                isCorrect = true,
                title = "Correct!",
                message = "+30 XP earned!",
                conceptTested = q.topic.title,
                explanation = q.explanation,
                xpEarned = 30,
                onDismiss = {
                    if (isLast) {
                        finishChallenge(newScore, state.totalQuestions)
                    } else {
                        _challengeState.value = state.copy(
                            score = newScore,
                            currentQuestionIndex = state.currentQuestionIndex + 1
                        )
                    }
                }
            )
            viewModelScope.launch {
                addXpAndSave(
                    profile.copy(
                        totalQuestionsAnswered = profile.totalQuestionsAnswered + 1,
                        totalCorrectAnswers = profile.totalCorrectAnswers + 1
                    ),
                    30
                )
            }
        } else {
            SoundManager.playIncorrect(profile.soundEnabled)
            val isLast = state.currentQuestionIndex + 1 >= state.totalQuestions

            viewModelScope.launch {
                repository.recordMistake(
                    question = q.question,
                    userAnswer = q.options.getOrElse(optionIndex) { "Option $optionIndex" },
                    correctAnswer = q.options[q.correctIndex],
                    explanation = q.explanation,
                    topic = q.topic.title
                )
                repository.saveProfile(
                    profile.copy(totalQuestionsAnswered = profile.totalQuestionsAnswered + 1)
                )
            }

            _activeFeedback.value = FeedbackData(
                isCorrect = false,
                title = "Incorrect",
                message = "The correct answer was: ${q.options[q.correctIndex]}",
                conceptTested = q.topic.title,
                explanation = q.explanation,
                onDismiss = {
                    if (isLast) {
                        finishChallenge(state.score, state.totalQuestions)
                    } else {
                        _challengeState.value = state.copy(
                            currentQuestionIndex = state.currentQuestionIndex + 1
                        )
                    }
                }
            )
        }
    }

    private fun finishChallenge(finalScore: Int, total: Int) {
        timerJob?.cancel()
        val state = _challengeState.value ?: return
        _challengeState.value = state.copy(isFinished = true, score = finalScore)
        SoundManager.playProteinComplete(userProfile.value.soundEnabled)

        if (finalScore == total) {
            val achievements = userProfile.value.unlockedAchievementIds.toMutableSet()
            if (!achievements.contains("quiz_master")) {
                achievements.add("quiz_master")
                notifyAchievement("Quiz Master Unlocked! +200 XP")
                viewModelScope.launch {
                    addXpAndSave(userProfile.value.copy(unlockedAchievementIds = achievements), 200)
                }
            }
        }
    }

    // --- DAILY CHALLENGE ---

    fun startDailyChallenge() {
        val today = ProteinRepository.getTodayDateString()
        if (userProfile.value.dailyChallengeCompletedDate == today) {
            _activeFeedback.value = FeedbackData(
                isCorrect = true,
                title = "Daily Challenge Completed!",
                message = "You have already completed today's challenge! Come back tomorrow for +100 bonus XP.",
                conceptTested = "Daily Biology Habit",
                explanation = "Consistent daily biology practice solidifies amino acid retention and structural intuition."
            )
            return
        }
        // Launch Mode 5 (Biology Quiz) with special daily bonus
        startChallengeMode(5)
    }

    fun markDailyChallengeDone() {
        val today = ProteinRepository.getTodayDateString()
        viewModelScope.launch {
            addXpAndSave(
                userProfile.value.copy(dailyChallengeCompletedDate = today),
                100
            )
        }
    }

    // --- FINAL MASTER EXAM ---

    fun startMasterExam() {
        val questions = QuestionBank.questions.shuffled().take(12)
        _masterExamState.value = MasterExamState(
            questions = questions,
            totalCount = questions.size
        )
        navigateTo(Screen.MasterExam)
    }

    fun submitExamAnswer(optionIndex: Int) {
        val exam = _masterExamState.value ?: return
        exam.userAnswers[exam.currentIndex] = optionIndex

        if (exam.currentIndex + 1 < exam.totalCount) {
            _masterExamState.value = exam.copy(currentIndex = exam.currentIndex + 1)
        } else {
            // Grade exam
            var correct = 0
            val topicSuccess = mutableMapOf<String, Int>()
            val topicTotal = mutableMapOf<String, Int>()

            exam.questions.forEachIndexed { i, q ->
                val chosen = exam.userAnswers[i]
                val tName = q.topic.title
                topicTotal[tName] = (topicTotal[tName] ?: 0) + 1
                if (chosen == q.correctIndex) {
                    correct++
                    topicSuccess[tName] = (topicSuccess[tName] ?: 0) + 1
                }
            }

            val percent = ((correct.toDouble() / exam.totalCount) * 100).toInt()
            val mastered = topicTotal.keys.filter { (topicSuccess[it] ?: 0) == topicTotal[it] }
            val needPractice = topicTotal.keys.filter { (topicSuccess[it] ?: 0) < (topicTotal[it] ?: 1) }
            val earnedXp = correct * 30 + if (percent >= 70) 300 else 50

            _masterExamState.value = exam.copy(
                isCompleted = true,
                scorePercent = percent,
                correctCount = correct,
                topicsMastered = mastered,
                topicsToPractice = needPractice,
                xpEarned = earnedXp
            )

            val profile = userProfile.value
            val passed = percent >= 70
            SoundManager.playProteinComplete(profile.soundEnabled)

            viewModelScope.launch {
                val achievements = profile.unlockedAchievementIds.toMutableSet()
                if (passed && !achievements.contains("master_exam_passed")) {
                    achievements.add("master_exam_passed")
                    notifyAchievement("Biology Champion! +500 XP")
                }
                addXpAndSave(
                    profile.copy(
                        masterExamPassed = passed || profile.masterExamPassed,
                        masterExamHighScore = maxOf(profile.masterExamHighScore, percent),
                        unlockedAchievementIds = achievements,
                        totalQuestionsAnswered = profile.totalQuestionsAnswered + exam.totalCount,
                        totalCorrectAnswers = profile.totalCorrectAnswers + correct
                    ),
                    earnedXp
                )
            }
        }
    }

    // --- MISTAKE PRACTICE ---

    fun startPracticeMistakes() {
        val mistakes = unresolvedMistakes.value
        if (mistakes.isEmpty()) {
            _activeFeedback.value = FeedbackData(
                isCorrect = true,
                title = "No Mistakes Logged!",
                message = "You have a flawless record or haven't made any mistakes yet. Keep it up!",
                conceptTested = "Perfect Accuracy",
                explanation = "Mistakes made during quizzes and building games will automatically appear here for focused review."
            )
            return
        }
        _currentPracticeIndex.value = 0
        navigateTo(Screen.MistakePractice)
    }

    fun submitPracticeMistakeAnswer(mistake: MistakeItem, answeredCorrectly: Boolean) {
        if (answeredCorrectly) {
            SoundManager.playCorrect(userProfile.value.soundEnabled)
            viewModelScope.launch {
                repository.resolveMistake(mistake.id)
                addXpAndSave(userProfile.value, 25)
            }
            _activeFeedback.value = FeedbackData(
                isCorrect = true,
                title = "Mistake Resolved! +25 XP",
                message = "You corrected this concept!",
                conceptTested = mistake.topic,
                explanation = mistake.explanation,
                onDismiss = {
                    if (_currentPracticeIndex.value + 1 >= unresolvedMistakes.value.size) {
                        navigateBack()
                    } else {
                        _currentPracticeIndex.value += 1
                    }
                }
            )
        } else {
            SoundManager.playIncorrect(userProfile.value.soundEnabled)
            _activeFeedback.value = FeedbackData(
                isCorrect = false,
                title = "Still Tricky",
                message = "Correct: ${mistake.correctAnswer}",
                conceptTested = mistake.topic,
                explanation = mistake.explanation
            )
        }
    }

    // --- HELPER: XP & LEVEL PROGRESSION ---

    private suspend fun addXpAndSave(profile: UserProfile, additionalXp: Int) {
        val oldLvl = profile.levelInfo.level
        val newXp = profile.xp + additionalXp
        val newLvl = LevelSystem.getLevelForXp(newXp).level

        if (newLvl > oldLvl) {
            SoundManager.playLevelUp(profile.soundEnabled)
            _celebrationNotice.value = "LEVEL UP! You are now Level $newLvl: ${LevelSystem.getLevelForXp(newXp).title}!"
        }

        repository.saveProfile(profile.copy(xp = newXp))
    }

    private fun notifyAchievement(text: String) {
        SoundManager.playAchievement(userProfile.value.soundEnabled)
        _celebrationNotice.value = text
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllData()
            _screenStack.value = listOf(Screen.Landing)
        }
    }

    // --- AI SEARCH GROUNDING (gemini-3.5-flash with googleSearch tool) ---
    private val _searchResult = MutableStateFlow<com.example.ai.BiologySearchResult?>(null)
    val searchResult: StateFlow<com.example.ai.BiologySearchResult?> = _searchResult.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun executeBioSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isSearching.value = true
            val result = com.example.ai.GeminiAiService.searchGroundedBiology(query.trim())
            _searchResult.value = result
            _isSearching.value = false
        }
    }

    fun clearSearchResult() {
        _searchResult.value = null
    }

    // --- VEO VIDEO GENERATION (veo-3.1-fast-generate-preview) ---
    private val _veoResult = MutableStateFlow<com.example.ai.VeoGenerationResult?>(null)
    val veoResult: StateFlow<com.example.ai.VeoGenerationResult?> = _veoResult.asStateFlow()

    private val _isGeneratingVeo = MutableStateFlow(false)
    val isGeneratingVeo: StateFlow<Boolean> = _isGeneratingVeo.asStateFlow()

    fun executeVeoGeneration(bitmap: android.graphics.Bitmap?, prompt: String, aspectRatio: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _isGeneratingVeo.value = true
            val result = com.example.ai.GeminiAiService.generateVeoVideo(bitmap, prompt.trim(), aspectRatio)
            _veoResult.value = result
            _isGeneratingVeo.value = false
        }
    }

    fun clearVeoResult() {
        _veoResult.value = null
    }

    // --- FIREBASE AUTH & FIRESTORE DATA PERSISTENCE ---
    private val _firebaseUser = MutableStateFlow<com.google.firebase.auth.FirebaseUser?>(com.example.firebase.FirebaseManager.currentUser)
    val firebaseUser: StateFlow<com.google.firebase.auth.FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _firestoreSyncStatus = MutableStateFlow<String?>(null)
    val firestoreSyncStatus: StateFlow<String?> = _firestoreSyncStatus.asStateFlow()

    fun signInWithGoogle(context: android.content.Context) {
        viewModelScope.launch {
            val result = com.example.firebase.FirebaseManager.signInWithGoogle(context)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _firebaseUser.value = user
                _firestoreSyncStatus.value = "Signed in as ${user?.displayName ?: "User"}! Syncing cloud data..."
                syncToFirestore()
            } else {
                _firestoreSyncStatus.value = "Sign-in error: ${result.exceptionOrNull()?.localizedMessage ?: "Unknown error"}"
            }
        }
    }

    fun signOutFromFirebase() {
        com.example.firebase.FirebaseManager.signOut()
        _firebaseUser.value = null
        _firestoreSyncStatus.value = "Signed out."
    }

    fun syncToFirestore() {
        viewModelScope.launch {
            val success = com.example.firebase.FirebaseManager.syncProfileToFirestore(userProfile.value)
            _firestoreSyncStatus.value = if (success) "✓ Data synced with Firestore!" else "Firestore sync pending (sign in to sync)"
        }
    }

    fun dismissFirestoreStatus() {
        _firestoreSyncStatus.value = null
    }
}
