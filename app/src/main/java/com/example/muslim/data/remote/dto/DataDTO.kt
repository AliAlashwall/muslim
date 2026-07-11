package com.example.muslim.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataDTO(
    @SerialName("date")
    val date: DateDTO,
    @SerialName("times")
    val times: TimesDTO,
    @SerialName("prohibited_times")
    val prohibitedTimes: ProhibitedTimesDTO,
    @SerialName("qibla")
    val qibla: QiblaDTO? = null,
    @SerialName("timezone")
    val timezone: TimezoneDTO? = null
)
