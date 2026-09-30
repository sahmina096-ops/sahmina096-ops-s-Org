package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BuildingDifficulty
import com.example.data.model.LessonData
import com.example.ui.components.FeedbackDialog
import com.example.ui.components.TopAppBarHeader
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.ChallengeGameScreen
import com.example.ui.screens.ChallengesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DifficultySelectScreen
import com.example.ui.screens.EncyclopediaScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.MasterExamScreen
import com.example.ui.screens.MistakePracticeScreen
import com.example.ui.screens.MistakeReviewScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.ProteinBuildingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StructureLabScreen
import com.example.ui.theme.AmberBond
import com.example.ui.theme.ProteinBuilderTheme
import com.example.viewmodel.ProteinViewModel
import com.example.viewmodel.Screen

import androidx.compose.ui.platform.LocalContext
import com.example.ui.screens.BioSearchScreen
import com.example.ui.screens.VeoAnimatorScreen

class MainActivity : ComponentActivity() {

    private val viewModel: ProteinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProteinBuilderTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: ProteinViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val unresolvedMistakes by viewModel.unresolvedMistakes.collectAsStateWithLifecycle()
    val buildingState by viewModel.buildingState.collectAsStateWithLifecycle()
    val challengeState by viewModel.challengeState.collectAsStateWithLifecycle()
    val masterExamState by viewModel.masterExamState.collectAsStateWithLifecycle()
    val currentPracticeIndex by viewModel.currentPracticeIndex.collectAsStateWithLifecycle()
    val activeFeedback by viewModel.activeFeedback.collectAsStateWithLifecycle()
    val celebrationNotice by viewModel.celebrationNotice.collectAsStateWithLifecycle()
    val firebaseUser by viewModel.firebaseUser.collectAsStateWithLifecycle()
    val firestoreSyncStatus by viewModel.firestoreSyncStatus.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val veoResult by viewModel.veoResult.collectAsStateWithLifecycle()
    val isGeneratingVeo by viewModel.isGeneratingVeo.collectAsStateWithLifecycle()

    // Handle back button on sub-screens
    BackHandler(enabled = currentScreen !is Screen.Landing) {
        viewModel.navigateBack()
    }

    Scaffold(
        topBar = {
            if (currentScreen !is Screen.Landing) {
                TopAppBarHeader(
                    userProfile = userProfile,
                    canNavigateBack = true,
                    onNavigateBack = { viewModel.navigateBack() },
                    onSoundToggle = { viewModel.setSoundEnabled(it) },
                    onTitleClick = { viewModel.navigateTo(Screen.Dashboard) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Landing -> {
                    LandingScreen(
                        onStartGame = { viewModel.navigateTo(Screen.DifficultySelect) },
                        onLearn = { viewModel.navigateTo(Screen.LearnList) },
                        onChallenges = { viewModel.navigateTo(Screen.ChallengesMenu) },
                        onProgress = { viewModel.navigateTo(Screen.ProgressStats) },
                        onAchievements = { viewModel.navigateTo(Screen.Achievements) },
                        onSettings = { viewModel.navigateTo(Screen.Settings) },
                        onDashboard = { viewModel.navigateTo(Screen.Dashboard) },
                        onBioSearch = { viewModel.navigateTo(Screen.BioSearch) },
                        onVeoAnimator = { viewModel.navigateTo(Screen.VeoAnimator) }
                    )
                }

                is Screen.Dashboard -> {
                    DashboardScreen(
                        userProfile = userProfile,
                        unresolvedMistakesCount = unresolvedMistakes.size,
                        firebaseUser = firebaseUser,
                        firestoreSyncStatus = firestoreSyncStatus,
                        onSignInGoogle = { viewModel.signInWithGoogle(context) },
                        onSignOutGoogle = { viewModel.signOutFromFirebase() },
                        onSyncFirestore = { viewModel.syncToFirestore() },
                        onContinueGame = { viewModel.navigateTo(Screen.DifficultySelect) },
                        onLearn = { viewModel.navigateTo(Screen.LearnList) },
                        onChallenges = { viewModel.navigateTo(Screen.ChallengesMenu) },
                        onDailyChallenge = { viewModel.startDailyChallenge() },
                        onEncyclopedia = { viewModel.navigateTo(Screen.Encyclopedia) },
                        onStructureLab = { viewModel.navigateTo(Screen.StructureLab) },
                        onProgress = { viewModel.navigateTo(Screen.ProgressStats) },
                        onReviewMistakes = { viewModel.navigateTo(Screen.MistakeReview) },
                        onMasterExam = { viewModel.startMasterExam() },
                        onBioSearch = { viewModel.navigateTo(Screen.BioSearch) },
                        onVeoAnimator = { viewModel.navigateTo(Screen.VeoAnimator) }
                    )
                }

                is Screen.DifficultySelect -> {
                    DifficultySelectScreen(
                        onSelectDifficulty = { diff -> viewModel.startProteinBuilder(diff) },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.ProteinBuilder -> {
                    val state = buildingState
                    if (state != null) {
                        ProteinBuildingScreen(
                            state = state,
                            onSelectAminoAcid = { viewModel.selectAminoAcidForChain(it) },
                            onUseHint = { viewModel.useBuilderHint() },
                            onQuit = { viewModel.navigateBack() }
                        )
                    } else {
                        DifficultySelectScreen(
                            onSelectDifficulty = { diff -> viewModel.startProteinBuilder(diff) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }
                }

                is Screen.LearnList -> {
                    LearnScreen(
                        completedLessonIds = userProfile.completedLessonIds,
                        onSelectLesson = { lessonId -> viewModel.navigateTo(Screen.LessonDetail(lessonId)) }
                    )
                }

                is Screen.LessonDetail -> {
                    val lesson = LessonData.lessons.find { it.id == screen.lessonId }
                    if (lesson != null) {
                        val isDone = userProfile.completedLessonIds.contains(lesson.id)
                        LessonDetailScreen(
                            lesson = lesson,
                            isCompleted = isDone,
                            onAnswerQuiz = { index -> viewModel.completeLessonQuiz(lesson.id, index) }
                        )
                    }
                }

                is Screen.Encyclopedia -> {
                    EncyclopediaScreen()
                }

                is Screen.StructureLab -> {
                    StructureLabScreen()
                }

                is Screen.ChallengesMenu -> {
                    ChallengesScreen(
                        onSelectMode = { modeId ->
                            if (modeId == 1) {
                                viewModel.navigateTo(Screen.DifficultySelect)
                            } else {
                                viewModel.startChallengeMode(modeId)
                            }
                        }
                    )
                }

                is Screen.ChallengeGame -> {
                    val state = challengeState
                    if (state != null) {
                        ChallengeGameScreen(
                            state = state,
                            onSubmitAnswer = { viewModel.submitChallengeAnswer(it) },
                            onFinish = { viewModel.navigateBack() }
                        )
                    }
                }

                is Screen.ProgressStats -> {
                    ProgressScreen(userProfile = userProfile)
                }

                is Screen.Achievements -> {
                    AchievementsScreen(unlockedIds = userProfile.unlockedAchievementIds)
                }

                is Screen.MistakeReview -> {
                    MistakeReviewScreen(
                        mistakes = unresolvedMistakes,
                        onPracticeMistakes = { viewModel.startPracticeMistakes() }
                    )
                }

                is Screen.MistakePractice -> {
                    val currentMistake = unresolvedMistakes.getOrNull(currentPracticeIndex)
                    if (currentMistake != null) {
                        MistakePracticeScreen(
                            mistake = currentMistake,
                            currentIndex = currentPracticeIndex,
                            totalCount = unresolvedMistakes.size,
                            onAnswer = { correct -> viewModel.submitPracticeMistakeAnswer(currentMistake, correct) }
                        )
                    } else {
                        MistakeReviewScreen(
                            mistakes = unresolvedMistakes,
                            onPracticeMistakes = { viewModel.startPracticeMistakes() }
                        )
                    }
                }

                is Screen.MasterExam -> {
                    val exam = masterExamState
                    if (exam != null) {
                        MasterExamScreen(
                            state = exam,
                            studentName = userProfile.name,
                            onSubmitAnswer = { viewModel.submitExamAnswer(it) },
                            onFinish = { viewModel.navigateTo(Screen.Dashboard) }
                        )
                    }
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        userProfile = userProfile,
                        onSaveName = { viewModel.setStudentName(it) },
                        onToggleSound = { viewModel.setSoundEnabled(it) },
                        onResetAllProgress = { viewModel.resetAllProgress() }
                    )
                }

                is Screen.BioSearch -> {
                    BioSearchScreen(
                        searchResult = searchResult,
                        isSearching = isSearching,
                        onSearch = { query -> viewModel.executeBioSearch(query) }
                    )
                }

                is Screen.BioSearchWithQuery -> {
                    BioSearchScreen(
                        searchResult = searchResult,
                        isSearching = isSearching,
                        initialQuery = screen.initialQuery,
                        onSearch = { query -> viewModel.executeBioSearch(query) }
                    )
                }

                is Screen.VeoAnimator -> {
                    VeoAnimatorScreen(
                        veoResult = veoResult,
                        isGenerating = isGeneratingVeo,
                        onGenerate = { bitmap, prompt, ratio ->
                            viewModel.executeVeoGeneration(bitmap, prompt, ratio)
                        }
                    )
                }
            }

            // Global Educational Feedback Dialog
            activeFeedback?.let { feedback ->
                FeedbackDialog(
                    feedback = feedback,
                    onDismiss = { viewModel.dismissFeedback() }
                )
            }

            // Floating Celebration Notification Banner
            celebrationNotice?.let { text ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { -it }),
                    exit = slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AmberBond,
                        shadowElevation = 6.dp,
                        modifier = Modifier.clickable { viewModel.dismissCelebration() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = text,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
