package com.example.muslim.domain.usecase

import com.example.muslim.domain.model.PrayerTimesForMonth
import com.example.muslim.domain.repository.PrayerTimesRepository
import com.example.muslim.domain.util.APIResult
import javax.inject.Inject

class GetPrayerTimesUseCase @Inject constructor(
    private val repository: PrayerTimesRepository
) {
    suspend operator fun invoke(date: String): APIResult<PrayerTimesForMonth> =
        repository.getPrayerTimesCurrentDay(date)
}