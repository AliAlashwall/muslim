package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeekdayDTO(
    @SerialName("en")
    val en: String
)