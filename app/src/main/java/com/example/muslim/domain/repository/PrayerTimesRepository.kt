package com.example.muslim.domain.repository

import com.example.muslim.domain.model.PrayerTimesForMonth
import com.example.muslim.domain.util.APIResult

interface PrayerTimesRepository {
    suspend fun getPrayerTimesCurrentDay(currentDay: String): APIResult<PrayerTimesForMonth>
}