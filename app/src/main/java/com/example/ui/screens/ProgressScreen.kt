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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.Achievement
import com.example.data.model.AchievementData
import com.example.data.model.LevelSystem
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA

@Composable
fun ProgressScreen(
    userProfile: UserProfile,
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
        Column {
            Text(
                text = "BIOLOGY PROGRESS & METRICS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = "Mastery Dashboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Level & XP Big Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Current Rank", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                        Text(text = userProfile.levelInfo.title, fontWeight = FontWeight.Black, fontSize = 20.sp, color = BioBlue)
                    }

                    Text(text = "Level ${userProfile.levelInfo.level}", fontWeight = FontWeight.Black, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(modifier = Modifier.height(12.dp))

                val progress = LevelSystem.getProgressInLevel(userProfile.xp)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = EmeraldBio,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${userProfile.xp} XP Earned", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Next: ${userProfile.levelInfo.maxXp} XP", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                }
            }
        }

        // Overall Accuracy & Learning Metrics
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Learning Performance", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                MetricProgressBar(
                    label = "Overall Accuracy",
                    valueText = "${userProfile.accuracyPercent}%",
                    progress = userProfile.accuracyPercent / 100f,
                    color = EmeraldBio
                )

                MetricProgressBar(
                    label = "Curriculum Lessons Completed",
                    valueText = "${userProfile.completedLessonIds.size} / 8",
                    progress = userProfile.completedLessonIds.size / 8f,
                    color = BioBlue
                )

                MetricProgressBar(
                    label = "Proteins Built",
                    valueText = "${userProfile.proteinsBuilt} Total",
                    progress = (userProfile.proteinsBuilt / 10f).coerceIn(0f, 1f),
                    color = VioletDNA
                )
            }
        }

        // Building Performance Breakdown (Easy / Medium / Hard)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Synthesis Breakdown by Difficulty", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DifficultyStatColumn("Easy", "${userProfile.easyProteinsBuilt}", EmeraldBio)
                    DifficultyStatColumn("Medium", "${userProfile.mediumProteinsBuilt}", BioBlue)
                    DifficultyStatColumn("Hard", "${userProfile.hardProteinsBuilt}", CrimsonAlert)
                }
            }
        }

        // Habit & Exam Stats
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Study Habits & Milestones", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Current Study Streak", fontSize = 13.sp)
                    Text(text = "🔥 ${userProfile.currentStreak} Days", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberBond)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Longest Streak Recorded", fontSize = 13.sp)
                    Text(text = "🏆 ${userProfile.longestStreak} Days", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Master Exam Status", fontSize = 13.sp)
                    Text(
                        text = if (userProfile.masterExamPassed) "Passed (${userProfile.masterExamHighScore}%)" else "Pending",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (userProfile.masterExamPassed) EmeraldBio else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MetricProgressBar(
    label: String,
    valueText: String,
    progress: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            Text(text = valueText, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color = color,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun DifficultyStatColumn(title: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontWeight = FontWeight.Black, fontSize = 22.sp, color = color)
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun AchievementsScreen(
    unlockedIds: Set<String>,
    modifier: Modifier = Modifier
) {
    val allAchievements = AchievementData.initialAchievements

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
                    text = "BIOLOGY BADGES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BioBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Achievements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AmberBond.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${unlockedIds.size} / ${allAchievements.size} Unlocked",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AmberBond,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(allAchievements, key = { it.id }) { ach ->
                val isUnlocked = unlockedIds.contains(ach.id)
                AchievementCard(achievement = ach, isUnlocked = isUnlocked)
            }
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: Achievement,
    isUnlocked: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievement_card_${achievement.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) AmberBond.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(text = achievement.iconEmoji, fontSize = 24.sp)
                } else {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), modifier = Modifier.size(22.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = achievement.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = achievement.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isUnlocked) 0.7f else 0.4f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isUnlocked) AmberBond.copy(alpha = 0.15f) else Color.Transparent
            ) {
                Text(
                    text = if (isUnlocked) "+${achievement.xpReward} XP" else "LOCKED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) AmberBond else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
