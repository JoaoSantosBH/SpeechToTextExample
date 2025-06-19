package com.jomar.ttsexample

data class GoogleSpeechRequest(
    val config: RecognitionConfig,
    val audio: RecognitionAudio
)
