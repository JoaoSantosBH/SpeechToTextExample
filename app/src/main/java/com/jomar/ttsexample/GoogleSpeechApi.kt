package com.jomar.ttsexample

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GoogleSpeechApi {
    @Headers("Content-Type: application/json")
    @POST("v1/speech:recognize")
    suspend fun recognizeSpeech(
        @retrofit2.http.Query("key") apiKey: String,
        @Body request: GoogleSpeechRequest
    ): GoogleSpeechResponse
}