package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimezoneDTO(
    @SerialName("abbreviation")
    val abbreviation: String,
    @SerialName("name")
    val name: String,
    @SerialName("utc_offset")
    val utcOffset: String
)