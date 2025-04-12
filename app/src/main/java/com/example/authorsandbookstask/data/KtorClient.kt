package com.example.authorsandbookstask.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    private val json = Json {
        ignoreUnknownKeys
        isLenient
    }
    val client:HttpClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}