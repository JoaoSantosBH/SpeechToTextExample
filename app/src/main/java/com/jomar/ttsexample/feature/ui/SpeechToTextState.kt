package com.jomar.ttsexample.feature.ui

data class SpeechToTextState(
    val isRecording: Boolean = false,
    val transcription: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFileName: String? = null
)