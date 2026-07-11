package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DistanceDTO(
    @SerialName("unit")
    val unit: String,
    @SerialName("value")
    val value: Double
)