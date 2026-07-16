package com.example.muslim.presentation.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.muslim.R
import com.example.muslim.presentation.components.PrayerIconBadge
import com.example.muslim.presentation.designSystem.theme.MuslimTheme
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.mapper.toDisplayTime
import com.example.muslim.presentation.mapper.toPrayerAr
import com.example.muslim.presentation.screens.home.components.AyahOfTheDayCard
import com.example.muslim.presentation.screens.home.components.FeatureGrid
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem
import com.example.muslim.presentation.screens.prayerTimes.PrayerStatus
import com.example.muslim.presentation.screens.prayerTimes.PrayerTimesUiState
import com.example.muslim.presentation.screens.prayerTimes.PrayerTimesViewModel
import kotlinx.coroutines.delay

// Colors that don't have a dedicated design-system token yet, following the same
// pattern used in PrayerTimesScreen.kt (local, file-scoped Color constants).

private val ProgressTrack = Color(0xFFE8E6DE)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    prayerViewModel: PrayerTimesViewModel = hiltViewModel(),
    onPrayersClicked: () -> Unit = {},
    onFeatureClick: (HomeFeature) -> Unit = {}
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val prayersUiState by prayerViewModel.uiState.collectAsStateWithLifecycle()

    // Design is Arabic-first, same convention as PrayerTimesScreen.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeaderSection(
                uiState = uiState,
                prayersUiState = prayersUiState,
                updateRemainingTime = { prayerViewModel.updateRemainingTime(it) },
                onPrayersClicked = { onPrayersClicked() }

            )

            AyahOfTheDayCard(
                ayah = uiState.ayah,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-60).dp)
            )


            FeatureGrid(
                features = uiState.features,
                onFeatureClick = onFeatureClick,
                onPrayersClicked = { onPrayersClicked() }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeHeaderSection(
    uiState: HomeUiState,
    prayersUiState: PrayerTimesUiState,
    updateRemainingTime: (String) -> Unit,
    onPrayersClicked: () -> Unit
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
                .padding(top = 12.dp, bottom = 100.dp) // extra room for the overlapping ayah card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.moshaf_sharif),
                        color = Theme.colors.onPrimary,
                        style = Theme.textStyle.title.large
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = uiState.greeting,
                        color = Theme.colors.onPrimary.copy(alpha = 0.8f),
                        style = Theme.textStyle.label.small
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            NextPrayerCard(
                uiState = uiState, prayersUiState = prayersUiState,
                updateRemainingTime = { updateRemainingTime(it) },
                onPrayersClicked = { onPrayersClicked() }
            )

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NextPrayerCard(
    uiState: HomeUiState,
    prayersUiState: PrayerTimesUiState,
    updateRemainingTime: (String) -> Unit,
    onPrayersClicked: () -> Unit
) {

    val closestPrayer =
        prayersUiState.prayers.find { prayerItem -> prayerItem.status == PrayerStatus.CLOSEST }

    LaunchedEffect(closestPrayer?.time) {
        while (closestPrayer?.time != null) {
            updateRemainingTime(closestPrayer.time)
            // Wait until the next minute starts to update again
            delay(60000L - (System.currentTimeMillis() % 60000L))
        }
    }
    Surface(
        color = Theme.colors.surface,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPrayersClicked() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Theme.colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = Theme.colors.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.next_prayer),
                            color = Theme.colors.onTertiary,
                            style = Theme.textStyle.label.small
                        )
                        Text(
                            text = "صلاة ${closestPrayer?.name?.toPrayerAr()}",
                            color = Theme.colors.onSurface,
                            style = Theme.textStyle.title.medium
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = prayersUiState.currentTime,
                        color = Theme.colors.primary,
                        fontSize = 30.sp,
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Theme.colors.primary)
                        )
                        Spacer(Modifier.width(4.dp))
                        Log.d("home screen", "NextPrayerCard: ${prayersUiState.remainingTime}")
                        Text(
                            text = " بعد ${prayersUiState.remainingTime}",
                            color = Theme.colors.onTertiary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            DayProgressBar(progress = uiState.dayProgress)

            Spacer(Modifier.height(40.dp))

            MiniPrayersRow(prayers = prayersUiState.prayers)
        }
    }
}

@Composable
private fun DayProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(50))
            .background(ProgressTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(Theme.colors.primary)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MiniPrayersRow(prayers: List<PrayerItem>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        prayers.forEach { prayer ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrayerIconBadge(icon = prayer.icon, iconSize = 12)

                Spacer(Modifier.height(6.dp))
                Text(
                    text = prayer.name.toPrayerAr(),
                    color = Theme.colors.onSurface,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = prayer.time.toDisplayTime(),
                    color = Theme.colors.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenPreview() {
    MuslimTheme {
        HomeScreen()
    }
}