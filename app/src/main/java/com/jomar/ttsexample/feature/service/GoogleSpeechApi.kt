package com.jomar.ttsexample.feature.service

import com.jomar.ttsexample.feature.model.GoogleSpeechRequest
import com.jomar.ttsexample.feature.model.GoogleSpeechResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleSpeechApi {
    @Headers("Content-Type: application/json")
    @POST("v1/speech:recognize")
    suspend fun recognizeSpeech(
        @Query("key") apiKey: String,
        @Body request: GoogleSpeechRequest
    ): GoogleSpeechResponse
}