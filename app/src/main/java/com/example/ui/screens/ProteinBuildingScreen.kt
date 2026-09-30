package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Lightbulb
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuildingDifficulty
import com.example.ui.components.AminoAcidNode
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.GrowingProteinChainViewer
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA
import com.example.viewmodel.ActiveBuildingState

@Composable
fun DifficultySelectScreen(
    onSelectDifficulty: (BuildingDifficulty) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SELECT DIFFICULTY",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = BioBlue,
            letterSpacing = 1.sp
        )
        Text(
            text = "Choose your synthesis challenge level",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        DifficultyCard(
            title = "EASY",
            description = "Fundamental peptide bond creation. 3-4 amino acids with guided hints and no time limit.",
            points = "100 Base XP",
            color = EmeraldBio,
            onClick = { onSelectDifficulty(BuildingDifficulty.EASY) },
            testTag = "difficulty_easy"
        )

        Spacer(modifier = Modifier.height(14.dp))

        DifficultyCard(
            title = "MEDIUM",
            description = "Longer chains (5-7 residues), biological peptide hormones, and fewer sequence hints.",
            points = "200 Base XP",
            color = BioBlue,
            onClick = { onSelectDifficulty(BuildingDifficulty.MEDIUM) },
            testTag = "difficulty_medium"
        )

        Spacer(modifier = Modifier.height(14.dp))

        DifficultyCard(
            title = "HARD",
            description = "8+ residues, 45-second countdown timer, complex folding motifs, and maximum rewards.",
            points = "350 Base XP",
            color = CrimsonAlert,
            onClick = { onSelectDifficulty(BuildingDifficulty.HARD) },
            testTag = "difficulty_hard"
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
private fun DifficultyCard(
    title: String,
    description: String,
    points: String,
    color: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.first().toString(),
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontWeight = FontWeight.Black, fontSize = 16.sp, color = color)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = color.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = points,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = color,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProteinBuildingScreen(
    state: ActiveBuildingState,
    onSelectAminoAcid: (String) -> Unit,
    onUseHint: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TARGET: ${state.recipe.name.uppercase()}",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = BioBlue
                    )
                    Text(
                        text = state.recipe.scientificName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
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
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (state.timeRemainingSec < 10) CrimsonAlert else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.timeRemainingSec}s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (state.timeRemainingSec < 10) CrimsonAlert else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Biological Target Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Target Sequence: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = state.recipe.sequence.joinToString(" - "),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = BioBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Clue: ${state.recipe.biologyHint}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Protein Chain Growing Visualizer
            GrowingProteinChainViewer(
                chain = state.builtChain,
                targetLength = state.recipe.sequence.size,
                justFormedIndex = state.justFormedBondIndex
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hint box if used
            if (state.hintText != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AmberBond.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = AmberBond, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = state.hintText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Options Amino Acid Rack
            Text(
                text = "Select Next Amino Acid Piece:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.optionPool.forEach { code3 ->
                    AminoAcidNode(
                        code3 = code3,
                        onClick = { onSelectAminoAcid(code3) },
                        modifier = Modifier.testTag("rack_node_$code3")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom controls: Hint & Quit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onUseHint,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("builder_hint_button")
                ) {
                    Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hint")
                }

                OutlinedButton(
                    onClick = onQuit,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp).testTag("builder_quit_button")
                ) {
                    Text("Quit")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Completion Overlay Dialog
        if (state.isCompleted) {
            ConfettiEffect()
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .testTag("completion_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(EmeraldBio.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldBio, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Protein Assembled!",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = EmeraldBio
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "+${state.earnedXp} XP Earned",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AmberBond
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = state.recipe.biologicalFunction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 Biology Fact: ${state.recipe.educationalFact}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onQuit,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("completion_continue_button")
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
