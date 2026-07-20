package com.example.muslim.data.remote.repository

import android.util.Log
import com.example.muslim.data.remote.dto.PrayerTimesPerDayDTO
import com.example.muslim.data.remote.mapper.toDomain
import com.example.muslim.data.util.HttpErrorHandler
import com.example.muslim.domain.model.PrayerTimesPerDay
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

    override suspend fun getPrayerTimesCurrentDay(currentDay: String): APIResult<PrayerTimesPerDay> {
        return try {
            val prayerTimes = httpClient.get("prayer-time") {
                parameter("api_key", Constants.API_KEY)
                parameter("date", currentDay)
                parameter("lat", "30.7833")
                parameter("lon", "31.0000")
                parameter("method", "3")
                parameter("school", "1")

            }.body<PrayerTimesPerDayDTO>().toDomain()
            Log.d(
                "PrayerTimesRepository",
                "Prayer Times comes successfully from Network: ${prayerTimes.data?.times}"
            )
            APIResult.Success(prayerTimes)
        } catch (e: Exception) {
            Log.e("PrayerTimesRepository", "Error fetching prayer times for the month: $e", e)
            HttpErrorHandler.handleException(e, "Failed to fetch prayer times for the month")
        }
    }
}