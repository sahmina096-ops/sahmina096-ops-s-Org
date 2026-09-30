package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiEffect
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import com.example.viewmodel.MasterExamState

@Composable
fun MasterExamScreen(
    state: MasterExamState,
    studentName: String,
    onSubmitAnswer: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isCompleted) {
        // Celebratory Exam Certificate Screen
        val scrollState = rememberScrollState()
        val passed = state.scorePercent >= 70

        Box(modifier = modifier.fillMaxSize()) {
            if (passed) {
                ConfettiEffect()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // In-Game Certificate Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = if (passed) AmberBond else VioletDNA.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .testTag("exam_certificate_card")
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (passed) Icons.Default.EmojiEvents else Icons.Default.School,
                            contentDescription = null,
                            tint = if (passed) AmberBond else VioletDNA,
                            modifier = Modifier.size(56.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "PROTEIN MASTER EXAM",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = VioletDNA,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = if (passed) "CERTIFICATE OF GAME MASTERY" else "EXAM COMPLETION REPORT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (passed) AmberBond else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Awarded in-game to",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Text(
                            text = studentName.uppercase(),
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = BioBlue
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ExamResultItem("Score", "${state.scorePercent}%", if (passed) EmeraldBio else VioletDNA)
                            ExamResultItem("Correct", "${state.correctCount}/${state.totalCount}", BioBlue)
                            ExamResultItem("XP Earned", "+${state.xpEarned}", AmberBond)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (passed)
                                "Congratulations! You have demonstrated exceptional mastery across amino acids, peptide linkages, structural hierarchy, and cellular synthesis."
                            else
                                "Good effort! Review the chapters and retry the exam to achieve a passing score of 70% or higher.",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Note: This is an in-game achievement certificate for educational simulation purposes.",
                            fontSize = 10.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                // Topics Mastered Card
                if (state.topicsMastered.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldBio.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "✅ Topics Mastered (100% Correct):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EmeraldBio)
                            Spacer(modifier = Modifier.height(4.dp))
                            state.topicsMastered.forEach { topic ->
                                Text(text = "• $topic", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Topics To Practice Card
                if (state.topicsToPractice.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AmberBond.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "📖 Recommended Areas for Practice:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberBond)
                            Spacer(modifier = Modifier.height(4.dp))
                            state.topicsToPractice.forEach { topic ->
                                Text(text = "• $topic", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Button(
                    onClick = onFinish,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("exam_finish_button")
                ) {
                    Text("Return to Dashboard", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        // Active Question Screen
        val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
        var selectedChoice by remember(state.currentIndex) { mutableStateOf<Int?>(null) }
        val progress = (state.currentIndex.toFloat() / state.totalCount)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PROTEIN MASTER EXAM",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VioletDNA
                    )
                    Text(
                        text = "Question ${state.currentIndex + 1} of ${state.totalCount}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = VioletDNA.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = currentQ.topic.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletDNA,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = VioletDNA,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentQ.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { index, option ->
                    val isSelected = selectedChoice == index
                    OutlinedButton(
                        onClick = { selectedChoice = index },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) VioletDNA.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("exam_option_$index")
                    ) {
                        Text(
                            text = option,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val choice = selectedChoice
                    if (choice != null) {
                        onSubmitAnswer(choice)
                    }
                },
                enabled = selectedChoice != null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VioletDNA),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("exam_next_button")
            ) {
                Text(
                    text = if (state.currentIndex + 1 == state.totalCount) "Submit Final Exam" else "Next Question",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun ExamResultItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
    }
}
