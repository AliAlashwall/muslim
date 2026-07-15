package com.example.muslim.presentation.screens.prayerTimes

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.muslim.R
import com.example.muslim.data.local.database.entity.AlarmEntity
import com.example.muslim.presentation.components.PrayerIconBadge
import com.example.muslim.presentation.components.SettingsIconButton
import com.example.muslim.presentation.designSystem.theme.MuslimTheme
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.mapper.get24Hours
import com.example.muslim.presentation.mapper.getMinutes
import com.example.muslim.presentation.mapper.toDisplayTime
import com.example.muslim.presentation.mapper.toPrayerAr
import kotlinx.coroutines.delay


private val TextMuted = Color(0xFFA3A3A3)
private val ToggleGreenOn = Color(0xFF2FA671)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrayerTimesScreen(
    modifier: Modifier = Modifier,
    viewModel: PrayerTimesViewModel,
    onSettingsClick: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Theme.colors.primary)
        }
    } else if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = uiState.error ?: "Unknown Error", color = Color.Red)
        }
    } else {
        uiState.header?.let { header ->
            PrayerTimesContent(
                modifier = modifier,
                header = header,
                prayers = uiState.prayers,
                remainingTime = uiState.remainingTime ?: "",
                onSettingsClick = onSettingsClick,
                getPrayerNotifState = { viewModel.getPrayerNotifState(it) },
                onToggleNotification = { alarm, value -> viewModel.toggleAlarm(alarm, value) },
                updateRemainingTime = { viewModel.updateRemainingTime(it) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition", "AutoboxingStateCreation")
@Composable
fun PrayerTimesContent(
    modifier: Modifier = Modifier,
    header: PrayerHeaderInfo,
    prayers: List<PrayerItem>,
    remainingTime: String,
    getPrayerNotifState: (String) -> Boolean,
    onSettingsClick: () -> Unit = {},
    onToggleNotification: (alarm: AlarmEntity, enabled: Boolean) -> Unit,
    updateRemainingTime: (String) -> Unit
) {
    val closestPrayerTime = prayers.find { it.status == PrayerStatus.CLOSEST }?.time
    val listState = rememberLazyListState()

    LaunchedEffect(closestPrayerTime) {
        while (closestPrayerTime != null) {
            updateRemainingTime(closestPrayerTime)
            // Wait until the next minute starts to update again
            delay(60000L - (System.currentTimeMillis() % 60000L))
        }
    }
    // The design is Arabic-first, so lay the whole screen out right-to-left.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Theme.colors.background)
        ) {
            PrayerHeaderSection(header = header, onSettingsClick = onSettingsClick)

            // Fixed: Moved animateScrollToItem into a LaunchedEffect
            LaunchedEffect(prayers) {
                val closestIndex = prayers.indexOfFirst { it.status == PrayerStatus.CLOSEST }
                if (closestIndex != -1) {
                    listState.animateScrollToItem(closestIndex)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
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
                    PrayerRowCard(
                        prayer = prayer, onToggle = onToggleNotification,
                        getPrayerNotifState = { getPrayerNotifState(it) },
                        remainingTime = remainingTime,
                    )
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
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Theme.colors.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.prayer_times),
                        color = Theme.colors.onPrimary,
                        style = Theme.textStyle.title.medium,
                    )
                    Text(
                        text = stringResource(R.string.exact_times_by_location),
                        color = Theme.colors.onPrimary.copy(alpha = 0.75f),
                        style = Theme.textStyle.label.small
                    )
                }
                SettingsIconButton(onClick = onSettingsClick)
            }

            Spacer(Modifier.height(18.dp))

            Surface(
                color = Theme.colors.onPrimary.copy(alpha = 0.14f),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = Theme.colors.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = header.locationLabel,
                        color = Theme.colors.onPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrayerRowCard(
    prayer: PrayerItem,
    getPrayerNotifState: (String) -> Boolean,
    onToggle: (AlarmEntity, Boolean) -> Unit,
    remainingTime: String? = "",
) {
    val isCLOSEST = remember { prayer.status == PrayerStatus.CLOSEST }
    val titleColor = if (isCLOSEST) Theme.colors.onPrimary else Theme.colors.onSurface
    val mutedColor = if (isCLOSEST) Theme.colors.onPrimary.copy(alpha = 0.85f) else TextMuted
    val isEnabled =
        remember(getPrayerNotifState) { mutableStateOf(getPrayerNotifState(prayer.name)) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(if (isCLOSEST) Theme.colors.primary else Theme.colors.surface)
                .then(
                    if (isCLOSEST)
                        Modifier.border(2.dp, Theme.colors.secondary, RoundedCornerShape(20.dp))
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PrayerIconBadge(icon = prayer.icon, isClosest = isCLOSEST)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = prayer.name.toPrayerAr(),
                            color = titleColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        when {
                            isCLOSEST && remainingTime != "" ->
                                Text(
                                    text = "متبقى $remainingTime",
                                    color = mutedColor,
                                    style = Theme.textStyle.label.small
                                )

                            prayer.status == PrayerStatus.UPCOMING ->
                                Text(
                                    text = stringResource(R.string.coming),
                                    color = mutedColor,
                                    style = Theme.textStyle.label.small
                                )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = prayer.time.toDisplayTime(),
                        color = titleColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                                    val alarm = AlarmEntity(
                                        id = prayer.id,
                                        date = "",
                                        hour = prayer.time.get24Hours(),
                                        minute = prayer.time.getMinutes(),
                                        label = prayer.name,
                                        isEnabled = it
                                    )
                                    onToggle(alarm, it)
                                },
                                modifier = Modifier.scale(0.75f),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Theme.colors.onPrimary,
                                    checkedTrackColor = if (isCLOSEST) Theme.colors.onPrimary.copy(
                                        alpha = 0.45f
                                    ) else ToggleGreenOn,
                                    checkedBorderColor = Color.Transparent,
                                    uncheckedThumbColor = Theme.colors.onPrimary,
                                    uncheckedTrackColor = if (isCLOSEST) Theme.colors.onPrimary.copy(
                                        alpha = 0.25f
                                    ) else Color(0xFFDEDEDA),
                                    uncheckedBorderColor = Color.Transparent
                                )
                            )

                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isEnabled.value) Icons.Outlined.Notifications else Icons.Outlined.NotificationsOff,
                                contentDescription = null,
                                tint = if (isCLOSEST) Theme.colors.onPrimary else Theme.colors.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }


    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun PrayerTimesScreenPreview() {
    MuslimTheme {
        PrayerHeaderSection(header = mockHeaderInfo(), onSettingsClick = {})
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PrayerRowCardPreview() {
    MuslimTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            PrayerRowCard(
                prayer = PrayerItem(
                    id = 1,
                    name = "الفجر",
                    time = "5:15",
                    icon = PrayerIcon.FAJR,
                    status = PrayerStatus.UPCOMING,

                    ),
                getPrayerNotifState = { true },
                onToggle = { _, _ -> },
                "20 دقيقة",
            )
        }
    }
}