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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.data.model.LessonData
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA

@Composable
fun LearnScreen(
    completedLessonIds: Set<Int>,
    onSelectLesson: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "BIOLOGY CURRICULUM",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Learn & Master (8 Chapters)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EmeraldBio.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${completedLessonIds.size} / 8 Done",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = EmeraldBio,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(LessonData.lessons, key = { it.id }) { lesson ->
                val isCompleted = completedLessonIds.contains(lesson.id)
                LessonItemCard(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    onClick = { onSelectLesson(lesson.id) }
                )
            }
        }
    }
}

@Composable
private fun LessonItemCard(
    lesson: Lesson,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lesson_card_${lesson.id}")
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) EmeraldBio.copy(alpha = 0.15f) else BioBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldBio, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "${lesson.chapterNumber}",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = BioBlue
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Chapter ${lesson.chapterNumber}: ${lesson.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lesson.subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }

            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                tint = if (isCompleted) EmeraldBio else BioBlue.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    isCompleted: Boolean,
    onAnswerQuiz: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chapter Header
        Column {
            Text(
                text = "CHAPTER ${lesson.chapterNumber}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = lesson.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = lesson.subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // Summary Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = BioBlue.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "📌 Summary: ${lesson.summary}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(14.dp)
            )
        }

        // Main Biological Content
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Explanation & Biology",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = lesson.mainContent,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }

        // Visual Diagram Callout
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = BioBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Visualizing the Concept", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.visualDiagramDescription,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        // Key Terms
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Key Biological Terms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                lesson.keyTerms.forEach { term ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(text = term.term, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BioBlue)
                        Text(text = term.definition, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    }
                }
            }
        }

        // Real-World Example & Quick Fact
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "🌍 Real-World Example:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EmeraldBio)
                Text(text = lesson.realWorldExample, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "⚡ Quick Fact:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberBond)
                Text(text = lesson.quickFact, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
            }
        }

        // Mini Quiz Section
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = VioletDNA.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth().testTag("lesson_mini_quiz")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Quiz, contentDescription = null, tint = VioletDNA)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chapter Mini-Quiz (+75 XP)",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = VioletDNA
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = lesson.miniQuiz.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                lesson.miniQuiz.options.forEachIndexed { index, option ->
                    val isSelected = selectedOption == index
                    OutlinedButton(
                        onClick = { selectedOption = index },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) VioletDNA.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("quiz_option_$index")
                    ) {
                        Text(
                            text = option,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val choice = selectedOption
                        if (choice != null) {
                            onAnswerQuiz(choice)
                        }
                    },
                    enabled = selectedOption != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletDNA),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_mini_quiz_button")
                ) {
                    Text("Check Answer", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
