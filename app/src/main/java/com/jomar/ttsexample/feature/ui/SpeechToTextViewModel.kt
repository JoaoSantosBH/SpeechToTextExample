package com.jomar.ttsexample.feature.ui

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomar.ttsexample.feature.domain.RecognitionAudio
import com.jomar.ttsexample.feature.domain.RecognitionConfig
import com.jomar.ttsexample.feature.model.GoogleSpeechRequest
import com.jomar.ttsexample.feature.service.GoogleSpeechApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.InputStream

class SpeechToTextViewModel : ViewModel() {
    private val _state = MutableStateFlow(SpeechToTextState())
    val state: StateFlow<SpeechToTextState> = _state.asStateFlow()

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    // Replace with your actual Google Cloud API key
    private val apiKey = "YOUR_GOOGLE_CLOUD_API_KEY"
    val BASE_URL = "https://speech.googleapis.com/v1/"

    private val speechApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleSpeechApi::class.java)

    fun startRecording(context: Context) {
        try {
            // Create audio file
            audioFile = File(context.cacheDir, "audio_recording.3gp")

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }

            _state.value = _state.value.copy(
                isRecording = true,
                error = null,
                transcription = ""
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = "Failed to start recording: ${e.message}"
            )
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            _state.value = _state.value.copy(isRecording = false)

            // Transcribe the recorded audio
            audioFile?.let { file ->
                transcribeRecordedAudio(file)
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isRecording = false,
                error = "Failed to stop recording: ${e.message}"
            )
        }
    }

    fun transcribeFromUri(context: Context, uri: Uri, fileName: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null,
                    selectedFileName = fileName
                )

                // Read audio file from URI
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val audioBytes = inputStream?.readBytes()
                inputStream?.close()

                if (audioBytes == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Failed to read audio file"
                    )
                    return@launch
                }

                // Convert audio to base64
                val base64Audio = Base64.encodeToString(audioBytes, Base64.NO_WRAP)

                // Determine audio encoding based on file extension
                val encoding = when (fileName.lowercase().substringAfterLast('.')) {
                    "mp3" -> "MP3"
                    "wav" -> "LINEAR16"
                    "flac" -> "FLAC"
                    "ogg" -> "OGG_OPUS"
                    "m4a", "aac" -> "MP3" // Fallback for AAC
                    "3gp" -> "AMR"
                    else -> "LINEAR16" // Default fallback
                }

                val sampleRate = when (encoding) {
                    "AMR" -> 8000
                    "LINEAR16" -> 16000
                    else -> 16000
                }

                // Create request
                val request = GoogleSpeechRequest(
                    config = RecognitionConfig(
                        encoding = encoding,
                        sampleRateHertz = sampleRate,
                        languageCode = "pt-BR"
                    ),
                    audio = RecognitionAudio(content = base64Audio)
                )

                // Make API call
                val response = speechApi.recognizeSpeech(apiKey = apiKey, request)

                val transcription = response.results?.firstOrNull()
                    ?.alternatives?.firstOrNull()?.transcript ?: "No speech detected"

                _state.value = _state.value.copy(
                    transcription = transcription,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Transcription failed: ${e.message}"
                )
            }
        }
    }

    private fun transcribeRecordedAudio(audioFile: File) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                // Convert audio file to base64
                val audioBytes = audioFile.readBytes()
                val base64Audio = Base64.encodeToString(audioBytes, Base64.NO_WRAP)

                // Create request
                val request = GoogleSpeechRequest(
                    config = RecognitionConfig(
                        encoding = "AMR",
                        sampleRateHertz = 8000,
                        languageCode = "en-US"
                    ),
                    audio = RecognitionAudio(content = base64Audio)
                )

                // Make API call
                val response = speechApi.recognizeSpeech(apiKey = apiKey,request)

                val transcription = response.results?.firstOrNull()
                    ?.alternatives?.firstOrNull()?.transcript ?: "No speech detected"

                _state.value = _state.value.copy(
                    transcription = transcription,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Transcription failed: ${e.message}"
                )
            }
        }
    }

    fun clearTranscription() {
        _state.value = _state.value.copy(
            transcription = "",
            selectedFileName = null
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}