package com.example.muslim.data.remote.repository

import android.util.Log
import com.example.muslim.data.remote.dto.PrayerTimesForMonthDTO
import com.example.muslim.data.remote.mapper.toDomain
import com.example.muslim.data.util.HttpErrorHandler
import com.example.muslim.domain.model.PrayerTimesForMonth
import com.example.muslim.domain.repository.PrayerTimesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import com.example.muslim.domain.util.APIResult
import com.example.muslim.util.Constants

class PrayerTimesRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : PrayerTimesRepository {

    override suspend fun getPrayerTimesForMonth(): APIResult<PrayerTimesForMonth> {
        return try {
            val prayerTimesForMonth = httpClient.get("prayer-time") {
                parameter("api_key", Constants.API_KEY)
                parameter("date", "2026-07-11")
                parameter("lat", "30.7833")
                parameter("lon", "31.0000")
                parameter("method", "3")
                parameter("school", "1")
//
            }.body<PrayerTimesForMonthDTO>().toDomain()

            APIResult.Success(prayerTimesForMonth)
        } catch (e: Exception) {
            Log.e("PrayerTimesRepository", "Error fetching prayer times for the month: $e", e)
            HttpErrorHandler.handleException(e, "Failed to fetch prayer times for the month")
        }
    }
}