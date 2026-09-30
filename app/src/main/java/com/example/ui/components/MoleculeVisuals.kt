package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AminoAcid
import com.example.data.model.AminoAcidData
import com.example.ui.theme.AaAcidicNegative
import com.example.ui.theme.AaNonPolar
import com.example.ui.theme.AaPolar
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AminoAcidNode(
    code3: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    showLabel: Boolean = true
) {
    val aa = remember(code3) { AminoAcidData.allAminoAcids.find { it.code3 == code3 } }
    val color = aa?.badgeColor ?: BioBlue

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag("amino_node_$code3")
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(54.dp)
                .shadow(elevation = if (isSelected) 6.dp else 3.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (isSelected) 3.dp else 1.5.dp,
                    color = if (isSelected) AmberBond else Color.White.copy(alpha = 0.8f),
                    shape = CircleShape
                )
        ) {
            // Subtle highlight glow
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.35f))
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = code3,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White
                )
                if (aa != null) {
                    Text(
                        text = aa.code1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        if (showLabel && aa != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = aa.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PeptideBondConnector(
    modifier: Modifier = Modifier,
    isNewlyFormed: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "bond")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        // Covalent bond bar
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AmberBond.copy(alpha = pulseAlpha))
        )
        Text(
            text = "CONH",
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = AmberBond
        )
        if (isNewlyFormed) {
            Text(
                text = "-H2O 💧",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = BioCyan
            )
        }
    }
}

@Composable
fun GrowingProteinChainViewer(
    chain: List<String>,
    targetLength: Int,
    modifier: Modifier = Modifier,
    justFormedIndex: Int? = null
) {
    val scrollState = rememberScrollState()

    // Auto-scroll to end as new bonds form
    LaunchedEffect(chain.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (chain.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ribosome Ready • Tap amino acids to form peptide bonds!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // N-terminus flag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BioBlue)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "N-Term",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    chain.forEachIndexed { index, code3 ->
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(tween(300, easing = FastOutSlowInEasing)) + fadeIn()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AminoAcidNode(
                                    code3 = code3,
                                    showLabel = false,
                                    isSelected = index == chain.lastIndex
                                )

                                if (index < chain.size - 1) {
                                    PeptideBondConnector(
                                        isNewlyFormed = index == justFormedIndex
                                    )
                                }
                            }
                        }
                    }

                    if (chain.size == targetLength) {
                        Spacer(modifier = Modifier.width(6.dp))
                        // C-terminus flag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AmberBond)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "C-Term",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Animated visualizer for the 4 Structure Levels (Primary, Secondary, Tertiary, Quaternary)
 */
@Composable
fun ProteinStructureVisualizer(
    level: Int, // 1, 2, 3, 4
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "structure")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        when (level) {
            1 -> {
                // PRIMARY: Straight line of beads with peptide bonds
                val numBeads = 7
                val spacing = w / (numBeads + 1)
                val y = cy
                val colors = listOf(BioBlue, EmeraldBio, AmberBond, VioletDNA, AaNonPolar, AaAcidicNegative, AaPolar)

                // Backbone line
                drawLine(
                    color = Color.LightGray,
                    start = Offset(spacing, y),
                    end = Offset(spacing * numBeads, y),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )

                for (i in 0 until numBeads) {
                    val x = spacing * (i + 1)
                    drawCircle(
                        color = colors[i % colors.size],
                        radius = 18f,
                        center = Offset(x, y)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f),
                        radius = 6f,
                        center = Offset(x - 5f, y - 5f)
                    )
                }
            }
            2 -> {
                // SECONDARY: Alpha-Helix (sine wave spiral) and Beta-Sheet pleated arrow
                val path = Path()
                val steps = 80
                val radPhase = Math.toRadians(phase.toDouble()).toFloat()

                // Alpha helix (top half)
                for (i in 0..steps) {
                    val t = i.toFloat() / steps
                    val x = 40f + t * (w - 80f)
                    val y = cy - 40f + sin(t * 14f + radPhase) * 25f
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path = path, color = BioBlue, style = Stroke(width = 8f, cap = StrokeCap.Round))

                // Beta-sheet arrow (bottom half)
                val sheetPath = Path().apply {
                    moveTo(40f, cy + 30f)
                    lineTo(w * 0.4f, cy + 45f)
                    lineTo(w * 0.7f, cy + 30f)
                    lineTo(w * 0.85f, cy + 40f)
                    lineTo(w * 0.7f, cy + 50f)
                    close()
                }
                drawPath(sheetPath, color = EmeraldBio)
            }
            3 -> {
                // TERTIARY: Folded Globular Polypeptide with disulfide cross-links and hydrophobic core
                // Hydrophobic core cluster
                drawCircle(color = BioCyan.copy(alpha = 0.35f), radius = 60f, center = Offset(cx, cy))

                val foldPath = Path()
                val points = listOf(
                    Offset(cx - 70f, cy - 30f),
                    Offset(cx - 20f, cy - 70f),
                    Offset(cx + 60f, cy - 50f),
                    Offset(cx + 70f, cy + 30f),
                    Offset(cx + 20f, cy + 60f),
                    Offset(cx - 50f, cy + 50f),
                    Offset(cx - 20f, cy)
                )
                foldPath.moveTo(points[0].x, points[0].y)
                for (p in points) {
                    foldPath.lineTo(p.x, p.y)
                }
                drawPath(foldPath, color = VioletDNA, style = Stroke(width = 9f, cap = StrokeCap.Round))

                // Disulfide bridge (S-S)
                drawLine(
                    color = AmberBond,
                    start = Offset(cx - 20f, cy - 70f),
                    end = Offset(cx + 20f, cy + 60f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            4 -> {
                // QUATERNARY: 4 interlocking subunits (e.g. Hemoglobin 2 Alpha, 2 Beta)
                val subunitRadius = 45f
                val offsetDist = 42f
                val subColors = listOf(BioBlue, BioCyan, EmeraldBio, VioletDNA)

                val centers = listOf(
                    Offset(cx - offsetDist, cy - offsetDist), // Subunit Alpha 1
                    Offset(cx + offsetDist, cy - offsetDist), // Subunit Beta 1
                    Offset(cx + offsetDist, cy + offsetDist), // Subunit Alpha 2
                    Offset(cx - offsetDist, cy + offsetDist)  // Subunit Beta 2
                )

                centers.forEachIndexed { i, c ->
                    drawCircle(color = subColors[i], radius = subunitRadius, center = c)
                    // Heme group in center
                    drawCircle(color = AmberBond, radius = 10f, center = c)
                }
            }
        }
    }
}
