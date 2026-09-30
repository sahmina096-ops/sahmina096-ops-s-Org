package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import kotlin.random.Random

private data class ConfettiParticle(
    val xRatio: Float,
    val initialYRatio: Float,
    val speed: Float,
    val color: Color,
    val size: Float,
    val angle: Float
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    val particles = remember {
        val colors = listOf(BioBlue, BioCyan, EmeraldBio, AmberBond, VioletDNA, Color(0xFFEC4899))
        List(50) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                initialYRatio = -0.1f - Random.nextFloat() * 0.3f,
                speed = 0.8f + Random.nextFloat() * 0.6f,
                color = colors.random(),
                size = 8f + Random.nextFloat() * 12f,
                angle = Random.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
        )
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val currentProgress = progress.value

            particles.forEach { p ->
                val currentY = (p.initialYRatio + currentProgress * p.speed) * h
                val currentX = p.xRatio * w + kotlin.math.sin(currentProgress * 8f + p.angle) * 30f

                drawRect(
                    color = p.color.copy(alpha = (1f - currentProgress * 0.8f).coerceIn(0f, 1f)),
                    topLeft = Offset(currentX, currentY),
                    size = Size(p.size, p.size * 0.6f)
                )
            }
        }
    }
}
