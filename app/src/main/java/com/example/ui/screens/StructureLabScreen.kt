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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.ProteinStructureVisualizer
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.EmeraldBio
import com.example.ui.theme.VioletDNA

private data class StructureLevelData(
    val levelNumber: Int,
    val title: String,
    val name: String,
    val summary: String,
    val primaryBonds: String,
    val drivingForces: String,
    val classicExamples: String,
    val clinicalInsight: String
)

private val structureLevels = listOf(
    StructureLevelData(
        levelNumber = 1,
        title = "PRIMARY (1°)",
        name = "Linear Amino Acid Sequence",
        summary = "The specific linear sequence of amino acids joined covalently from the N-terminus to C-terminus.",
        primaryBonds = "Covalent Peptide Bonds (-CO-NH-)",
        drivingForces = "Genetic transcription/translation code executed by the ribosome.",
        classicExamples = "Insulin A-chain (21 AAs), Glucagon (29 AAs), Ribonuclease A.",
        clinicalInsight = "A single point mutation changing glutamate (Glu) to valine (Val) at position 6 of beta-globin causes sickle-cell anemia."
    ),
    StructureLevelData(
        levelNumber = 2,
        title = "SECONDARY (2°)",
        name = "Alpha-Helices & Beta-Sheets",
        summary = "Regular, repeating local conformations of the polypeptide backbone.",
        primaryBonds = "Hydrogen Bonds along the polypeptide backbone (C=O --- H-N)",
        drivingForces = "Backbone dihedral angle optimization (Ramachandran angles phi & psi).",
        classicExamples = "Alpha-helix in hair Keratin; Beta-pleated sheets in spider dragline Silk.",
        clinicalInsight = "Prion diseases (Mad Cow, Kuru) occur when normal alpha-helices anomalously misfold into contagious, insoluble beta-sheet plaques."
    ),
    StructureLevelData(
        levelNumber = 3,
        title = "TERTIARY (3°)",
        name = "3D Globular Conformation",
        summary = "The full three-dimensional spatial arrangement of a single complete polypeptide chain.",
        primaryBonds = "Hydrophobic interactions, Disulfide bridges (S-S), Ionic salt bridges, Hydrogen bonds between R-groups",
        drivingForces = "The Hydrophobic Effect burying non-polar side chains deep inside the interior core away from water.",
        classicExamples = "Myoglobin (oxygen carrier in muscle), Lysozyme, GFP (Green Fluorescent Protein).",
        clinicalInsight = "Fever and extreme body temperature denatures tertiary bonds, collapsing enzyme catalytic pockets."
    ),
    StructureLevelData(
        levelNumber = 4,
        title = "QUATERNARY (4°)",
        name = "Multi-Subunit Assembly",
        summary = "The geometric assembly of two or more independent polypeptide chains functioning as a single macromolecular oligomer.",
        primaryBonds = "Non-covalent interface interactions, hydrophobic patches, salt bridges, and interchain disulfide bonds",
        drivingForces = "Allosteric cooperativity and multienzyme efficiency.",
        classicExamples = "Hemoglobin (α2β2 tetramer), Collagen triple-helix, Antibodies (2 heavy + 2 light chains).",
        clinicalInsight = "Allosteric binding in hemoglobin allows cooperative oxygen uptake in lungs and immediate release in oxygen-starved muscles."
    )
)

@Composable
fun StructureLabScreen(
    modifier: Modifier = Modifier
) {
    var selectedLevelIndex by remember { mutableIntStateOf(0) }
    val currentLevel = structureLevels[selectedLevelIndex]
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "INTERACTIVE STRUCTURE LAB",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = "The 4 Structural Levels",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Level Tabs
        TabRow(
            selectedTabIndex = selectedLevelIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            structureLevels.forEachIndexed { index, level ->
                Tab(
                    selected = selectedLevelIndex == index,
                    onClick = { selectedLevelIndex = index },
                    modifier = Modifier.testTag("structure_tab_${level.levelNumber}"),
                    text = {
                        Text(
                            text = "${level.levelNumber}°",
                            fontWeight = if (selectedLevelIndex == index) FontWeight.Black else FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (selectedLevelIndex == index) BioBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Visualizer Canvas Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProteinStructureVisualizer(level = currentLevel.levelNumber)
                }
            }

            // Overview Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${currentLevel.title}: ${currentLevel.name}",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = BioBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentLevel.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 20.sp
                    )
                }
            }

            // Chemical Forces Details
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LabDetailItem("Key Chemical Bonds", currentLevel.primaryBonds, AmberBond)
                    LabDetailItem("Thermodynamic Driving Force", currentLevel.drivingForces, BioCyan)
                    LabDetailItem("Biological Examples", currentLevel.classicExamples, EmeraldBio)
                }
            }

            // Clinical / Medical Relevance
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VioletDNA.copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔬 Clinical & Medical Relevance:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = VioletDNA
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentLevel.clinicalInsight,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LabDetailItem(title: String, description: String, accentColor: Color) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = accentColor)
        Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
    }
}
