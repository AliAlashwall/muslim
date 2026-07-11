package com.example.cineverse.util

fun Int?.toRuntimeFormat(): String {
    if (this == null || this <= 0) return ""

    val hours = this / 60
    val minutes = this % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}