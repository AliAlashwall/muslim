package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SunsetDTO(
    @SerialName("end")
    val end: String,
    @SerialName("start")
    val start: String
)