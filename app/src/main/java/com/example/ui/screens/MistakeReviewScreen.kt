package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MistakeItem
import com.example.ui.theme.BioBlue
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio

@Composable
fun MistakeReviewScreen(
    mistakes: List<MistakeItem>,
    onPracticeMistakes: () -> Unit,
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
                    text = "TARGETED REVIEW",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Review Mistakes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (mistakes.isNotEmpty()) CrimsonAlert.copy(alpha = 0.15f) else EmeraldBio.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${mistakes.size} Logged",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (mistakes.isNotEmpty()) CrimsonAlert else EmeraldBio,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (mistakes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(EmeraldBio.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldBio, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Flawless Record!",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No unresolved mistakes logged. Great job!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                }
            }
        } else {
            Button(
                onClick = onPracticeMistakes,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("practice_my_mistakes_button")
            ) {
                Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Practice My Mistakes (${mistakes.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(mistakes, key = { it.id }) { mistake ->
                    MistakeItemCard(mistake = mistake)
                }
            }
        }
    }
}

@Composable
private fun MistakeItemCard(mistake: MistakeItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = BioBlue.copy(alpha = 0.1f)
            ) {
                Text(
                    text = mistake.topic,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = mistake.question,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Your Answer: ", fontSize = 12.sp, color = CrimsonAlert, fontWeight = FontWeight.Bold)
                Text(text = mistake.userAnswer, fontSize = 12.sp, color = CrimsonAlert)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Correct Answer: ", fontSize = 12.sp, color = EmeraldBio, fontWeight = FontWeight.Bold)
                Text(text = mistake.correctAnswer, fontSize = 12.sp, color = EmeraldBio)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 ${mistake.explanation}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun MistakePracticeScreen(
    mistake: MistakeItem,
    currentIndex: Int,
    totalCount: Int,
    onAnswer: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PRACTICING MISTAKE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Item ${currentIndex + 1} of $totalCount",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = BioBlue.copy(alpha = 0.12f)
            ) {
                Text(
                    text = mistake.topic,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Review Question:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mistake.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Biological Explanation:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = BioBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mistake.explanation,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Text(
            text = "Do you now understand and know the correct answer (${mistake.correctAnswer})?",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onAnswer(false) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(50.dp).testTag("practice_still_unsure")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = CrimsonAlert)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Still Unsure")
            }

            Button(
                onClick = { onAnswer(true) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldBio),
                modifier = Modifier.weight(1f).height(50.dp).testTag("practice_i_got_it")
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("I Got It! (+25 XP)", fontWeight = FontWeight.Bold)
            }
        }
    }
}
