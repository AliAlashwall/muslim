package com.example.muslim.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@RequiresApi(Build.VERSION_CODES.O)
private val timeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.HOUR_OF_DAY)
    .appendLiteral(':')
    .appendValue(ChronoField.MINUTE_OF_HOUR)
    .toFormatter()

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeDuration(t1: String, t2: String): String {
    //t1 and t2 HH:MM
    val duration = getDurationBetween(t1, t2)

    val hour = if (duration.toHours() > 0L) {
        duration.toHours().toString().toArabicDigits() + " ساعة "
    } else ""
    val minutes = (duration.toMinutes() % 60).toString().toArabicDigits()
    val remainingTime = "$hour $minutes دقيقة "

    Log.d("getTimeDuration", "getTimeDuration: $t1 - $t2 = $remainingTime")

    return remainingTime
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getDurationBetween(t1: String, t2: String): Duration {
    val time1 = try {
        LocalTime.parse(t1, timeFormatter)
    } catch (e: Exception) {
        LocalTime.parse(t1)
    }
    val time2 = try {
        LocalTime.parse(t2, timeFormatter)
    } catch (e: Exception) {
        LocalTime.parse(t2)
    }
    var duration = Duration.between(time1, time2)
    if (duration.isNegative) {
        duration = duration.plusDays(1)
    }
    return duration
}
