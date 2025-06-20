package com.jomar.ttsexample.feature.model

import com.jomar.ttsexample.feature.domain.SpeechResult

data class GoogleSpeechResponse(
    val results: List<SpeechResult>?
)