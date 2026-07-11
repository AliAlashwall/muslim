package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthXDTO(
    @SerialName("ar")
    val ar: String,
    @SerialName("days")
    val days: Int,
    @SerialName("en")
    val en: String,
    @SerialName("number")
    val number: Int
)