package com.example.muslim.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.screens.prayerTimes.PrayerIcon

private val IconBadgeLight = Color(0xFFF1F0EB)

@Composable
fun PrayerIconBadge(icon: PrayerIcon, isClosest: Boolean) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (isClosest) Theme.colors.onPrimary.copy(alpha = 0.18f) else IconBadgeLight),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon.toEmoji(), fontSize = 19.sp)
    }
}

// Swap this out for real drawable/Lottie assets whenever you're ready -
// the model layer only knows the icon *type*, never its representation.
fun PrayerIcon.toEmoji(): String = when (this) {
    PrayerIcon.FAJR -> "🌄"
    PrayerIcon.SUNRISE -> "☀️"
    PrayerIcon.DHUHR -> "⛅"
    PrayerIcon.ASR -> "🌤️"
    PrayerIcon.MAGHRIB -> "🌇"
    PrayerIcon.ISHA -> "🌙"
}