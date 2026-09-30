package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.BioBlue
import com.example.ui.theme.CrimsonAlert

@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onSaveName: (String) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onResetAllProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var showResetDialog by remember { mutableStateOf(false) }
    var nameSavedNotice by remember { mutableStateOf(false) }
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
                text = "GAME PREFERENCES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BioBlue,
                letterSpacing = 1.sp
            )
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Student Profile Settings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Student Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                        nameSavedNotice = false
                    },
                    label = { Text("Student Name") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("setting_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onSaveName(nameInput)
                        nameSavedNotice = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BioBlue),
                    modifier = Modifier.fillMaxWidth().testTag("save_name_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Name", fontWeight = FontWeight.Bold)
                }

                if (nameSavedNotice) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "✓ Name saved successfully!", color = BioBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Sound Settings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = BioBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Sound Effects", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = "Synthesized feedback tones & fanfares", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                    }
                }

                Switch(
                    checked = userProfile.soundEnabled,
                    onCheckedChange = { onToggleSound(it) },
                    modifier = Modifier.testTag("setting_sound_switch")
                )
            }
        }

        // About the App
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "About Protein Builder", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "A comprehensive biology education system covering all 20 standard amino acids, peptide dehydration synthesis, hierarchical folding structures (1° to 4°), molecular functions, and ribosome-mediated protein translation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Version 1.0.0 • Offline Local Database Enabled",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Danger Zone: Reset All Progress
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CrimsonAlert.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Reset Data", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CrimsonAlert)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Erases all XP, level ranks, streaks, mistake logs, and unlocked achievements from device storage.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonAlert),
                    modifier = Modifier.fillMaxWidth().testTag("reset_progress_button")
                ) {
                    Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset All Progress", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Progress?") },
            text = { Text("Are you sure you want to permanently erase all XP, achievements, streaks, and mistake records? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        onResetAllProgress()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert)
                ) {
                    Text("Yes, Reset Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
