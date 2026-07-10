package com.example.muslim.presentation.designSystem.typography

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle


data class MuslimTextStyle(
    val headline: SizedTextStyle,
    val title: SizedTextStyle,
    val body: SizedTextStyle,
    val label: SizedTextStyle,
)

data class SizedTextStyle(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

val LocalMuslimTextStyle = staticCompositionLocalOf { DefaultTextStyle }
