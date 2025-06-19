package com.jomar.ttsexample

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


// SpeechToTextScreen.kt
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SpeechToTextScreen(
    viewModel: SpeechToTextViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Request audio recording permission
    val audioPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )

    // File picker launcher for audio files
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            // Get file name from URI
            val fileName = context.contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(nameIndex)
            } ?: "Unknown file"

            viewModel.transcribeFromUri(context, selectedUri, fileName)
        }
    }

    LaunchedEffect(Unit) {
        if (!audioPermissionState.status.isGranted) {
            audioPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Speech to Text",
            style = MaterialTheme.typography.headlineMedium
        )

        if (!audioPermissionState.status.isGranted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Audio recording permission is required for live recording",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Action buttons row
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recording Button
            Button(
                onClick = {
                    if (state.isRecording) {
                        viewModel.stopRecording()
                    } else {
                        viewModel.startRecording(context)
                    }
                },
                enabled = audioPermissionState.status.isGranted && !state.isLoading,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (state.isRecording) "Stop Recording" else "Start Recording",
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "OR",
                style = MaterialTheme.typography.bodyLarge
            )

            // File picker button
            Button(
                onClick = {
                    audioPickerLauncher.launch("audio/*")
                },
                enabled = !state.isLoading,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = "Select Audio File",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = when {
                state.isRecording -> "Recording... Tap to stop"
                state.isLoading && state.selectedFileName != null -> "Transcribing ${state.selectedFileName}..."
                state.isLoading -> "Transcribing..."
                else -> "Record live audio or select a file"
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        // Loading indicator
        if (state.isLoading) {
            CircularProgressIndicator()
        }

        // Transcription result
        if (state.transcription.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transcription:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(
                            onClick = { viewModel.clearTranscription() }
                        ) {
                            Text("Clear")
                        }
                    }

                    if (state.selectedFileName != null) {
                        Text(
                            text = "File: ${state.selectedFileName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.transcription,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Error message
        state.error?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Error:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { viewModel.clearError() }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }

        // Instructions
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Instructions:\n" +
                        "1. Make sure you have a Google Cloud API key\n" +
                        "2. Replace YOUR_GOOGLE_CLOUD_API_KEY in the code\n" +
                        "3. Enable Speech-to-Text API in Google Cloud Console\n" +
                        "4. For live recording: Tap microphone, speak, then tap stop\n" +
                        "5. For pre-recorded files: Tap folder icon and select audio file\n" +
                        "6. Supported formats: MP3, WAV, FLAC, OGG, M4A, 3GP",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}