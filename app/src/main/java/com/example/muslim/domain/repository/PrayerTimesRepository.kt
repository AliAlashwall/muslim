package com.example.muslim.domain.repository

import com.example.muslim.domain.model.PrayerTimesForMonth
import com.example.muslim.domain.util.APIResult

interface PrayerTimesRepository {
    suspend fun getPrayerTimesForMonth(): APIResult<PrayerTimesForMonth>
}