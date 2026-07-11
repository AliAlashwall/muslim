package com.example.muslim.domain.model

data class Date(
    val readable: String,
    val timestamp: String,
    val gregorian: Gregorian,
    val hijri: Hijri
)