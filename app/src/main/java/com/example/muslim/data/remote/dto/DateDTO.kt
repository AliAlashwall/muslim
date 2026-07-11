package com.example.muslim.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateDTO(
    @SerialName("readable")
    val readable: String,
    @SerialName("timestamp")
    val timestamp: String,
    @SerialName("gregorian")
    val gregorian: GregorianDTO,
    @SerialName("hijri")
    val hijri: HijriDTO
)