package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimesPerDayDTO(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("data")
    val data: DataDTO? = null,
    @SerialName("status")
    val status: String? = null
)
