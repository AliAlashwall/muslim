package com.example.muslim.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.convertENMonthToAr(): String {
    return when (this) {
        "January" -> "يناير"
        "February" -> "فبراير"
        "March" -> "مارس"
        "April" -> "أبريل"
        "May" -> "مايو"
        "June" -> "يونيو"
        "July" -> "يوليو"
        "August" -> "أغسطس"
        "September" -> "سبتمبر"
        "October" -> "أكتوبر"
        "November" -> "نوفمبر"
        "December" -> "ديسمبر"
        else -> this
    }
}


fun String.convertENDayToAr(): String {
    return when (this) {
        "Saturday" -> "السبت"
        "Sunday" -> "الأحد"
        "Monday" -> "الإثنين"
        "Tuesday" -> "الثلاثاء"
        "Wednesday" -> "الأربعاء"
        "Thursday" -> "الخميس"
        "Friday" -> "الجمعة"
        else -> this
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.time12Hour(): String {
    val (hour24, minute) = this.split(":")
    val time24 = LocalTime.of(hour24.toInt(), minute.toInt())
    val formatter = DateTimeFormatter.ofPattern("hh:mm")
    return time24.format(formatter)
}