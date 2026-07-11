package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProhibitedTimesDTO(
    @SerialName("noon")
    val noon: NoonDTO,
    @SerialName("sunrise")
    val sunrise: SunriseDTO,
    @SerialName("sunset")
    val sunset: SunsetDTO
)
