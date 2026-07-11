package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeekdayXDTO(
    @SerialName("ar")
    val ar: String,
    @SerialName("en")
    val en: String
)