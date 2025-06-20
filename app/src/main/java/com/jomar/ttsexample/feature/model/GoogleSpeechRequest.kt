package com.jomar.ttsexample.feature.model

import com.jomar.ttsexample.feature.domain.RecognitionAudio
import com.jomar.ttsexample.feature.domain.RecognitionConfig

data class GoogleSpeechRequest(
    val config: RecognitionConfig,
    val audio: RecognitionAudio
)
