package com.example.muslim.presentation.screens.prayerTimes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslim.R
import com.example.muslim.presentation.components.PrayerIconBadge
import com.example.muslim.presentation.components.SettingsIconButton
import com.example.muslim.presentation.designSystem.theme.Theme


private val GoldAccent = Color(0xFFE2B33F)
private val ScreenBackground = Color(0xFFF4F3F1)
private val TextMuted = Color(0xFFA3A3A3)
private val ToggleGreenOn = Color(0xFF2FA671)


/**
 * Stateless prayer-times screen. Feed it fresh [PrayerHeaderInfo] / [PrayerUiState]
 * data - e.g. from a ViewModel's StateFlow backed by your prayer-times API - and
 * it just renders it. No networking, persistence, or "current prayer" logic
 * happens in here; that all belongs upstream, closer to your data source.
 */
@Composable
fun PrayerTimesScreen(
    header: PrayerHeaderInfo,
    prayers: List<PrayerUiState>,
    onSettingsClick: () -> Unit = {},
    onToggleNotification: (id: String, enabled: Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    // The design is Arabic-first, so lay the whole screen out right-to-left.
    // This also mirrors the Material icons/switch automatically.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ScreenBackground)
        ) {
            PrayerHeaderSection(header = header, onSettingsClick = onSettingsClick)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Spacer(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .size(width = 48.dp, height = 5.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Theme.colors.primary.copy(alpha = 0.25f))
                        )
                    }
                }
                items(prayers, key = { it.id }) { prayer ->
                    PrayerRowCard(prayer = prayer, onToggle = onToggleNotification)
                }
            }
        }
    }
}

@Composable
fun PrayerHeaderSection(
    header: PrayerHeaderInfo,
    onSettingsClick: () -> Unit
) {
    Surface(
        color = Theme.colors.primary,
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // keeps content clear of the status bar under edge-to-edge
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 28.dp)
        ) {
            // Top row: arrow at the right edge, title centered, settings at the left edge (RTL).
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Theme.colors.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.prayer_times),
                        color = Theme.colors.onPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = stringResource(R.string.exact_times_by_location),
                        color = Theme.colors.onPrimary.copy(alpha = 0.75f),
                        style = Theme.textStyle.label.small
                    )
                }
                SettingsIconButton(onClick = onSettingsClick)
            }

            Spacer(Modifier.height(18.dp))

            // Location pill.
            Surface(
                color = Theme.colors.onPrimary.copy(alpha = 0.14f),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = header.locationLabel,
                        color = Theme.colors.onPrimary,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = Theme.colors.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Gregorian / Hijri date card.
            Surface(
                color = Theme.colors.onPrimary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        header.dayOfWeek,
                        color = Theme.colors.onPrimary.copy(alpha = 0.8f),
                        style = Theme.textStyle.body.small
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = header.gregorianDate,
                        color = Theme.colors.onPrimary,
                        style = Theme.textStyle.title.medium
                    )
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(
                        color = Theme.colors.onPrimary.copy(alpha = 0.18f),
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        header.hijriDate,
                        color = Theme.colors.onPrimary.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun PrayerRowCard(
    prayer: PrayerUiState,
    onToggle: (String, Boolean) -> Unit
) {
    val isCurrent = prayer.status == PrayerStatus.CURRENT
    val titleColor = if (isCurrent) Theme.colors.onPrimary else Theme.colors.onSurface
    val mutedColor = if (isCurrent) Theme.colors.onPrimary.copy(alpha = 0.85f) else TextMuted

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isCurrent) Theme.colors.primary else Theme.colors.surface)
            .then(
                if (isCurrent)
                    Modifier.border(2.dp, GoldAccent, RoundedCornerShape(20.dp))
                else
                    Modifier
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon + name/status - lands on the right edge under RTL.
            Row(verticalAlignment = Alignment.CenterVertically) {
                PrayerIconBadge(icon = prayer.icon, isCurrent = isCurrent)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = prayer.name,
                        color = titleColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    when {
                        isCurrent && prayer.countdownLabel != null ->
                            Text(
                                text = "• ${prayer.countdownLabel}",
                                color = mutedColor,
                                style = Theme.textStyle.label.small
                            )

                        prayer.status == PrayerStatus.UPCOMING ->
                            Text(
                                text = "قادمة",
                                color = mutedColor,
                                style = Theme.textStyle.label.small
                            )
                    }
                }
            }

            // Time + toggle - lands on the left edge under RTL.
            Column {
                Text(
                    text = prayer.time,
                    color = titleColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                val isEnabled = remember { mutableStateOf(prayer.isNotificationEnabled) }
                if (prayer.hasNotificationToggle) {
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isEnabled.value) {
                                stringResource(R.string.enabled)
                            } else stringResource(R.string.not_enabled),
                            color = mutedColor,
                            fontSize = 11.sp
                        )
                        Spacer(Modifier.width(6.dp))
                        Switch(
                            checked = isEnabled.value,
                            onCheckedChange = {
                                isEnabled.value = !isEnabled.value
                                onToggle(prayer.id, it)
                            },
                            modifier = Modifier.scale(0.75f),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Theme.colors.onPrimary,
                                checkedTrackColor = if (isCurrent) Theme.colors.onPrimary.copy(alpha = 0.45f) else ToggleGreenOn,
                                checkedBorderColor = Color.Transparent,
                                uncheckedThumbColor = Theme.colors.onPrimary,
                                uncheckedTrackColor = if (isCurrent) Theme.colors.onPrimary.copy(
                                    alpha = 0.25f
                                ) else Color(0xFFDEDEDA),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                        if (isCurrent) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = null,
                                tint = Theme.colors.onPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Extra "time remaining" footer, only for the current prayer.
        if (isCurrent) {
            val remaining = prayer.remainingFraction
            if (remaining != null) {
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = Theme.colors.onPrimary.copy(alpha = 0.2f))
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.remaining_time),
                        color = Theme.colors.onPrimary.copy(alpha = 0.85f),
                        style = Theme.textStyle.label.small
                    )
                    Text(
                        text = "${(remaining * 100).toInt()}%",
                        color = Theme.colors.onPrimary,
                        style = Theme.textStyle.label.small,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { remaining },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = GoldAccent,
                    trackColor = Theme.colors.onPrimary.copy(alpha = 0.25f)
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun PrayerTimesScreenPreview() {
    MaterialTheme {
        PrayerTimesScreen(
            header = sampleHeaderInfo(),
            prayers = samplePrayerTimes()
        )
    }
}