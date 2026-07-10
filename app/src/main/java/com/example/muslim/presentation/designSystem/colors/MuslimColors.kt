package com.example.muslim.presentation.designSystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MuslimColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val primaryContainer: Color,
    val secondaryContainer: Color,
)

val LocalMuslimColors = staticCompositionLocalOf { lightThemeColors }