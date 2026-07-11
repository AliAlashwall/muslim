package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GregorianDTO(
    @SerialName("date")
    val date: String,
    @SerialName("day")
    val day: String,
    @SerialName("designation")
    val designation: DesignationDTO,
    @SerialName("format")
    val format: String,
    @SerialName("month")
    val month: MonthDTO,
    @SerialName("weekday")
    val weekday: WeekdayDTO,
    @SerialName("year")
    val year: String
)