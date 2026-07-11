package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DesignationDTO(
    @SerialName("abbreviated")
    val abbreviated: String,
    @SerialName("expanded")
    val expanded: String
)