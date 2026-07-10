package com.example.muslim.presentation.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import com.example.muslim.presentation.designSystem.colors.LocalMuslimColors
import com.example.muslim.presentation.designSystem.colors.MuslimColors
import com.example.muslim.presentation.designSystem.colors.lightThemeColors
import com.example.muslim.presentation.designSystem.typography.DefaultTextStyle
import com.example.muslim.presentation.designSystem.typography.LocalMuslimTextStyle
import com.example.muslim.presentation.designSystem.typography.MuslimTextStyle

@Composable
fun MuslimTheme(
    state: ThemeState = rememberThemeState(isDark = false), //this app only support light theme
    content: @Composable () -> Unit
) {
    val colorScheme = lightThemeColors

    CompositionLocalProvider(
        LocalThemeState provides state,
        LocalMuslimColors provides colorScheme,
        LocalMuslimTextStyle provides DefaultTextStyle,
    ) {
        content()
    }
}

@Composable
fun rememberThemeState(
    isDark: Boolean = isSystemInDarkTheme(),
): ThemeState {
    val isDarkState = remember { mutableStateOf(isDark) }

    SideEffect {
        isDarkState.value = isDark
    }
    return remember(isDarkState.value) {
        ThemeState(
            isDark = isDarkState.value,
            onThemeChanged = { isDarkState.value = it }
        )
    }
}

object Theme {
    val colors: MuslimColors
        @Composable @ReadOnlyComposable get() = LocalMuslimColors.current

    val textStyle: MuslimTextStyle
        @Composable @ReadOnlyComposable get() = LocalMuslimTextStyle.current

    val state: ThemeState
        @Composable get() = LocalThemeState.current
}

val LocalThemeState = compositionLocalOf { ThemeState(false, {}) }
