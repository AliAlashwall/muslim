package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QiblaDTO(
    @SerialName("direction")
    val direction: DirectionDTO,
    @SerialName("distance")
    val distance: DistanceDTO
)