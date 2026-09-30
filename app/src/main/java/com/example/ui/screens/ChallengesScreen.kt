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
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiEffect
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import com.example.viewmodel.ActiveChallengeState

data class ModeMenuItem(
    val modeId: Int,
    val title: String,
    val subtitle: String,
    val difficulty: String,
    val xpText: String,
    val icon: ImageVector,
    val accentColor: Color
)

private val modesList = listOf(
    ModeMenuItem(1, "Build the Protein", "Assemble target peptide sequences with proper bonds", "Variable", "100-350 XP", Icons.Default.Build, BioBlue),
    ModeMenuItem(2, "Identify the Amino Acid", "Solve biochemical clues to deduce the 20 residues", "Medium", "30 XP/Q", Icons.Default.Biotech, EmeraldBio),
    ModeMenuItem(3, "Peptide Bond Challenge", "Master condensation, H2O loss, and resonance", "Medium", "30 XP/Q", Icons.Default.Link, AmberBond),
    ModeMenuItem(4, "Protein Structure Challenge", "Classify 1°, 2°, 3°, and 4° folding phenomena", "Medium", "30 XP/Q", Icons.Default.Science, VioletDNA),
    ModeMenuItem(5, "Biology Quiz", "Mixed random questions testing all protein concepts", "Comprehensive", "30 XP/Q", Icons.Default.Psychology, BioCyan),
    ModeMenuItem(6, "Speed Challenge (60s)", "Rapid-fire blitz against the clock with streak multiplier", "Hard", "Bonus XP", Icons.Default.Speed, CrimsonAlert),
    ModeMenuItem(7, "Mystery Protein Investigation", "Deduce medical and biological identity from clues", "Medium", "30 XP/Q", Icons.AutoMirrored.Filled.Help, Color(0xFFE11D48))
)

@Composable
fun ChallengesScreen(
    onSelectMode: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column {
            Text(
                text = "GAME MODES & CHALLENGES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = "7 Interactive Game Modes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        modesList.forEach { mode ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("challenge_mode_${mode.modeId}")
                    .clickable { onSelectMode(mode.modeId) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(mode.accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = mode.icon, contentDescription = null, tint = mode.accentColor, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mode ${mode.modeId}: ${mode.title}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = mode.accentColor.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = mode.xpText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = mode.accentColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = mode.subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ChallengeGameScreen(
    state: ActiveChallengeState,
    onSubmitAnswer: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isFinished) {
        // Result Screen
        Box(modifier = modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            ConfettiEffect()
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth().testTag("challenge_results_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Challenge Complete!",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = BioBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = state.modeTitle,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = CircleShape,
                        color = EmeraldBio.copy(alpha = 0.15f),
                        modifier = Modifier.size(90.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${state.score} / ${state.totalQuestions}",
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                color = EmeraldBio
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val earned = state.score * 30
                    Text(
                        text = "+$earned XP Earned",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AmberBond
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onFinish,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Return to Challenges", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // Active Question
        val q = state.questions.getOrNull(state.currentQuestionIndex) ?: return

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = state.modeTitle.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = BioBlue
                    )
                    Text(
                        text = "Question ${state.currentQuestionIndex + 1} of ${state.totalQuestions}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                if (state.timeRemainingSec != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (state.timeRemainingSec < 10) CrimsonAlert.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = if (state.timeRemainingSec < 10) CrimsonAlert else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${state.timeRemainingSec}s", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BioBlue.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = q.topic.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BioBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = q.question,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                q.options.forEachIndexed { index, option ->
                    OutlinedButton(
                        onClick = { onSubmitAnswer(index) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("challenge_option_$index")
                    ) {
                        Text(
                            text = option,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
