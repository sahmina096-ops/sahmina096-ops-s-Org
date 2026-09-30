package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.LevelSystem
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.EmeraldBio

@Composable
fun TopAppBarHeader(
    userProfile: UserProfile,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .testTag("top_bar_back_button")
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Column(modifier = Modifier.clickable { onTitleClick() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PROTEIN",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = BioBlue,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "BUILDER",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = userProfile.levelInfo.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Streak badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AmberBond.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔥", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${userProfile.currentStreak}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AmberBond
                        )
                    }

                    // Level & XP chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BioCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LVL ${userProfile.levelInfo.level} • ${userProfile.xp} XP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = BioBlue
                        )
                    }

                    // Sound Toggle
                    IconButton(
                        onClick = { onSoundToggle(!userProfile.soundEnabled) },
                        modifier = Modifier
                            .testTag("sound_toggle_button")
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (userProfile.soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = if (userProfile.soundEnabled) "Mute" else "Unmute",
                            tint = if (userProfile.soundEnabled) BioBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Level progress bar
            val progress = LevelSystem.getProgressInLevel(userProfile.xp)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = EmeraldBio,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
    }
}
