package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectionDTO(
    @SerialName("clockwise")
    val clockwise: Boolean,
    @SerialName("degrees")
    val degrees: Double,
    @SerialName("from")
    val from: String
)