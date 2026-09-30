package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import kotlin.math.sin

@Composable
fun LandingScreen(
    onStartGame: () -> Unit,
    onLearn: () -> Unit,
    onChallenges: () -> Unit,
    onProgress: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit,
    onDashboard: () -> Unit,
    onBioSearch: () -> Unit,
    onVeoAnimator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Rotating/pulsing helix animation
    val transition = rememberInfiniteTransition(label = "landing")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Hero Molecular Graphic
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(BioCyan.copy(alpha = 0.25f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(110.dp)) {
                val rad = Math.toRadians(angle.toDouble()).toFloat()
                val cx = size.width / 2
                val cy = size.height / 2

                val nodes = 6
                for (i in 0 until nodes) {
                    val a = rad + (i * (2 * Math.PI / nodes)).toFloat()
                    val x = cx + sin(a.toDouble()).toFloat() * 38f
                    val y = cy + kotlin.math.cos(a.toDouble()).toFloat() * 38f

                    val colors = listOf(BioBlue, EmeraldBio, AmberBond, VioletDNA, BioCyan, Color(0xFFF43F5E))
                    drawCircle(
                        color = colors[i % colors.size],
                        radius = 12f,
                        center = Offset(x, y)
                    )
                    // Connector line to center
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(cx, cy),
                        end = Offset(x, y),
                        strokeWidth = 3f
                    )
                }

                // Center Alpha Carbon
                drawCircle(color = BioBlue, radius = 16f, center = Offset(cx, cy))
                drawCircle(color = Color.White.copy(alpha = 0.8f), radius = 6f, center = Offset(cx - 4f, cy - 4f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "PROTEIN BUILDER",
            fontWeight = FontWeight.Black,
            fontSize = 32.sp,
            letterSpacing = 1.5.sp,
            color = BioBlue,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Build. Learn. Master Biology.",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Main primary buttons
        Button(
            onClick = onStartGame,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("start_game_button")
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Start Game",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onDashboard,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldBio),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("dashboard_button")
        ) {
            Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Student Dashboard",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLearn,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("learn_biology_button")
        ) {
            Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = BioBlue)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Learn Biology (8 Chapters)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onChallenges,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("challenges_button")
        ) {
            Icon(imageVector = Icons.Default.SportsEsports, contentDescription = null, tint = VioletDNA)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Challenges (7 Game Modes)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onBioSearch,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BioBlue.copy(alpha = 0.9f)),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("landing_biosearch_button")
            ) {
                Text(text = "🔍 BioSearch AI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Button(
                onClick = onVeoAnimator,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VioletDNA),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("landing_veo_button")
            ) {
                Text(text = "🎬 Veo Animator", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onProgress,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("progress_button")
            ) {
                Icon(imageVector = Icons.Default.Equalizer, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Progress", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onAchievements,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("achievements_button")
            ) {
                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = AmberBond, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Badges", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onSettings,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(0.7f)
                    .height(48.dp)
                    .testTag("settings_button")
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
