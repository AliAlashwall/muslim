package com.example.muslim.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {

        return HttpClient(Android) {

            // ── Engine config ──────────────────────────────────────────────
            engine {
                connectTimeout = 10_000       // ms to establish connection
                socketTimeout = 30_000       // ms to wait for data
            }

            // ── JSON ───────────────────────────────────────────────────────
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true  // don't crash on extra fields
                    isLenient = true  // accept malformed JSON (carefully)
                    prettyPrint = false
                    encodeDefaults = true  // include null fields in output
                })
            }

            // ── Retry ──────────────────────────────────────────────────────
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3, retryOnTimeout = true)
                exponentialDelay()           // waits 2s, 4s, 8s between retries
            }

            // ── Timeout ────────────────────────────────────────────────────
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 30_000
            }

            // ── Default request ────────────────────────────────────────────
            defaultRequest {
                url("https://islamicapi.com/api/v1/")
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }

            // ── Response validation ────────────────────────────────────────
            expectSuccess = true  // throws exceptions for 4xx/5xx

        }
    }

}