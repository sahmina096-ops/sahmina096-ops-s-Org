package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.BiologySearchResult
import com.example.ui.theme.AmberBond
import com.example.ui.theme.BioBlue
import com.example.ui.theme.BioCyan
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.EmeraldBio

private val sampleBiologyQueries = listOf(
    "How does sickle-cell anemia mutation change beta-globin structure?",
    "Recent discoveries in protein folding and AlphaFold",
    "Role of chaperone proteins in Alzheimer's amyloid-beta aggregation",
    "How do aminoglycoside antibiotics inhibit bacterial ribosomes?",
    "Biochemical mechanism of insulin receptor tyrosine kinase"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BioSearchScreen(
    searchResult: BiologySearchResult?,
    isSearching: Boolean,
    initialQuery: String = "",
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var queryText by remember { mutableStateOf(initialQuery) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank() && searchResult == null) {
            queryText = initialQuery
            onSearch(initialQuery)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BioBlue.copy(alpha = 0.15f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "GEMINI-3.5-FLASH • GOOGLE SEARCH GROUNDED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BioBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "BioSearch AI Research",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Up-to-date scientific explanations grounded directly with real-time Google Search data.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // Search Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("Search proteins, mutations, enzymes, or diseases...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BioBlue) },
                    trailingIcon = {
                        if (queryText.isNotEmpty()) {
                            IconButton(onClick = { queryText = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("biosearch_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { onSearch(queryText) },
                    enabled = queryText.isNotBlank() && !isSearching,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("biosearch_submit_button")
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Grounding with Google Search...")
                    } else {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search with Google Grounding", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Suggested Queries
        Text(
            text = "Suggested Research Topics:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sampleBiologyQueries.forEach { query ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable {
                            queryText = query
                            onSearch(query)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = AmberBond, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = query, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Search Results Section
        if (searchResult != null) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("biosearch_results_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (searchResult.isError) CrimsonAlert.copy(alpha = 0.15f) else EmeraldBio.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (searchResult.isError) Icons.Default.Close else Icons.Default.Language,
                                contentDescription = null,
                                tint = if (searchResult.isError) CrimsonAlert else EmeraldBio,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Grounded Answer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Query: \"${searchResult.query}\"",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = searchResult.content,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )

                    // Grounded Search Queries Used
                    if (searchResult.searchQueries.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "🔍 Google Search Queries Executed:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = BioBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                searchResult.searchQueries.forEach { q ->
                                    Text(text = "• \"$q\"", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
                                }
                            }
                        }
                    }

                    // Cited Web Sources
                    if (searchResult.sources.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "📚 Grounded Web Sources & Citations:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = EmeraldBio
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            searchResult.sources.forEach { source ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BioBlue.copy(alpha = 0.08f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source.uri))
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = source.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BioBlue,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                            contentDescription = "Open Link",
                                            tint = BioBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
