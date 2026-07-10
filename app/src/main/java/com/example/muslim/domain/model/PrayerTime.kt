package com.example.muslim.domain.model

data class PrayerTime(
    val name: String,
    val time: String,
    val arabicName: String? = null
)

data class PrayerTimesResponse(
    val date: String,
    val prayerTimes: List<PrayerTime>,
    val city: String? = null,
    val country: String? = null
)
