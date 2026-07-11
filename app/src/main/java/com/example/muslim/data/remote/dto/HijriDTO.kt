package com.example.muslim.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HijriDTO(
    @SerialName("adjustedHolidays")
    val adjustedHolidays: List<String>,
    @SerialName("date")
    val date: String,
    @SerialName("day")
    val day: String,
    @SerialName("designation")
    val designation: DesignationDTO,
    @SerialName("format")
    val format: String,
    @SerialName("holidays")
    val holidays: List<String>,
    @SerialName("method")
    val method: String,
    @SerialName("month")
    val month: MonthXDTO,
    @SerialName("shift")
    val shift: Int,
    @SerialName("weekday")
    val weekday: WeekdayXDTO,
    @SerialName("year")
    val year: String
)