package com.jomar.ttsexample.feature.domain

data class RecognitionConfig(
    val encoding: String = "WEBM_OPUS",
    val sampleRateHertz: Int = 16000,
    val languageCode: String = "en-US"
)