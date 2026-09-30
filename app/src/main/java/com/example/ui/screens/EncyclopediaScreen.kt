package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AminoAcid
import com.example.data.model.AminoAcidCategory
import com.example.data.model.AminoAcidData
import com.example.ui.components.AminoAcidNode
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.EmeraldBio

enum class EncyclopediaFilter(val label: String) {
    ALL("All 20"),
    ESSENTIAL("Essential"),
    NON_ESSENTIAL("Non-Essential"),
    NON_POLAR("Non-polar"),
    POLAR("Polar"),
    CHARGED("Charged"),
    SPECIAL("Special")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EncyclopediaScreen(
    onResearchAminoAcid: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(EncyclopediaFilter.ALL) }
    var selectedAminoAcid by remember { mutableStateOf<AminoAcid?>(null) }

    val filteredList = remember(searchQuery, selectedFilter) {
        AminoAcidData.allAminoAcids.filter { aa ->
            val matchesQuery = aa.name.contains(searchQuery, ignoreCase = true) ||
                    aa.code3.contains(searchQuery, ignoreCase = true) ||
                    aa.code1.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                EncyclopediaFilter.ALL -> true
                EncyclopediaFilter.ESSENTIAL -> aa.isEssential
                EncyclopediaFilter.NON_ESSENTIAL -> !aa.isEssential
                EncyclopediaFilter.NON_POLAR -> aa.category == AminoAcidCategory.NON_POLAR || aa.category == AminoAcidCategory.AROMATIC
                EncyclopediaFilter.POLAR -> aa.category == AminoAcidCategory.POLAR_UNCHARGED
                EncyclopediaFilter.CHARGED -> aa.category == AminoAcidCategory.POSITIVELY_CHARGED || aa.category == AminoAcidCategory.NEGATIVELY_CHARGED
                EncyclopediaFilter.SPECIAL -> aa.category == AminoAcidCategory.SPECIAL
            }
            matchesQuery && matchesFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "AMINO ACID ENCYCLOPEDIA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = "The 20 Proteinogenic Residues",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, 3-letter, or 1-letter code...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("encyclopedia_search_bar")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            EncyclopediaFilter.entries.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BioBlue.copy(alpha = 0.2f),
                        selectedLabelColor = BioBlue
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Amino Acid Grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList, key = { it.name }) { aa ->
                AminoAcidCard(
                    aminoAcid = aa,
                    onClick = { selectedAminoAcid = aa }
                )
            }
        }

        // Detail Dialog
        selectedAminoAcid?.let { aa ->
            AminoAcidDetailDialog(
                aminoAcid = aa,
                onDismiss = { selectedAminoAcid = null },
                onResearch = onResearchAminoAcid
            )
        }
    }
}

@Composable
private fun AminoAcidCard(
    aminoAcid: AminoAcid,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("aa_card_${aminoAcid.code3}")
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Essential badge
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (aminoAcid.isEssential) AmberBond.copy(alpha = 0.2f) else EmeraldBio.copy(alpha = 0.2f),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (aminoAcid.isEssential) "ESSENTIAL" else "NON-ESS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (aminoAcid.isEssential) AmberBond else EmeraldBio,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            AminoAcidNode(code3 = aminoAcid.code3, showLabel = false)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = aminoAcid.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = aminoAcid.category.displayName,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AminoAcidDetailDialog(
    aminoAcid: AminoAcid,
    onDismiss: () -> Unit,
    onResearch: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("aa_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (aminoAcid.isEssential) AmberBond.copy(alpha = 0.2f) else EmeraldBio.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (aminoAcid.isEssential) "ESSENTIAL (DIETARY)" else "NON-ESSENTIAL (SYNTHESIZED)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (aminoAcid.isEssential) AmberBond else EmeraldBio,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AminoAcidNode(code3 = aminoAcid.code3, showLabel = false)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = aminoAcid.name,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${aminoAcid.code3} • 1-Letter: ${aminoAcid.code1}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = BioBlue
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        DetailRow("Category", aminoAcid.category.displayName)
                        DetailRow("Side Chain (R)", aminoAcid.sideChain)
                        DetailRow("Formula", aminoAcid.formula)
                        DetailRow("Molecular Wt", "${aminoAcid.molecularWeight} g/mol")
                        DetailRow("Codons", aminoAcid.codons.joinToString(", "))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BioBlue)
                    Text(text = aminoAcid.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))

                    Text(text = "Biological Relevance", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EmeraldBio)
                    Text(text = aminoAcid.biologicalRelevance, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onResearch("Biological functions, metabolism, and clinical significance of amino acid ${aminoAcid.name} (${aminoAcid.code3})")
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("🔍 Research with Google Search AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
