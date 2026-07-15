package com.example.muslim.domain.repository

import com.example.muslim.domain.model.PrayerTimesPerDay
import com.example.muslim.domain.util.APIResult

interface PrayerTimesRepository {
    suspend fun getPrayerTimesCurrentDay(currentDay: String): APIResult<PrayerTimesPerDay>
}