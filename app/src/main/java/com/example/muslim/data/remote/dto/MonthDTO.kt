package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthDTO(
    @SerialName("en")
    val en: String,
    @SerialName("number")
    val number: Int
)