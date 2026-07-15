package com.example.muslim.presentation.screens

import android.os.Build
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
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslim.presentation.components.SettingsIconButton
import com.example.muslim.presentation.designSystem.theme.MuslimTheme
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.mapper.toDisplayTime
import com.example.muslim.presentation.mapper.toPrayerAr
import com.example.muslim.presentation.screens.prayerTimes.PrayerIcon
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem
import com.example.muslim.presentation.screens.prayerTimes.PrayerStatus

// Colors that don't have a dedicated design-system token yet, following the same
// pattern used in PrayerTimesScreen.kt (local, file-scoped Color constants).
private val TextMuted = Color(0xFFA3A3A3)
private val ProgressTrack = Color(0xFFE8E6DE)
private val AyahCardBackground = Color(0xFFF3E7C4)
private val AyahAccent = Color(0xFFC9A227)
private val GoldAccent = Color(0xFFC9A227)
private val GoldAccentBg = Color(0xFFFBF1D6)

/**
 * UI state for the home screen. In a real ViewModel this would be built from
 * GetPrayerTimesUseCase (for [miniPrayers]/[nextPrayerName]) + GetAyahOfDayUseCase.
 */
data class HomeUiState(
    val greeting: String = "السلام عليكم ورحمة الله",
    val currentTime: String = "12:30",
    val nextPrayerName: String = "صلاة الظهر",
    val remainingLabel: String = "بعد ساعة و 15 دقيقة",
    val dayProgress: Float = 0.62f,
    val miniPrayers: List<PrayerItem> = emptyList(),
    val ayah: AyahOfDay = AyahOfDay(),
    val features: List<HomeFeature> = defaultHomeFeatures()
)

data class AyahOfDay(
    val text: String = "وَقُلْ رَّبِّ زِدْنِي عِلْمًا",
    val reference: String = "سورة طه - آية 114"
)

data class HomeFeature(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: HomeFeatureAccent
)

enum class HomeFeatureAccent { GOLD, GREEN }

fun defaultHomeFeatures(): List<HomeFeature> = listOf(
    HomeFeature(1, "الأذان", "مواقيت الصلاة", Icons.Outlined.Schedule, HomeFeatureAccent.GOLD),
    HomeFeature(2, "المصحف", "قراءة القرآن الكريم", Icons.AutoMirrored.Outlined.MenuBook, HomeFeatureAccent.GREEN),
    HomeFeature(3, "التذكيرات", "تنبيهات يومية", Icons.Outlined.Notifications, HomeFeatureAccent.GOLD),
    HomeFeature(4, "الأدعية والأذكار", "حصن المسلم", Icons.Outlined.PanTool, HomeFeatureAccent.GREEN),
    HomeFeature(5, "المساجد", "أقرب المساجد", Icons.Outlined.LocationOn, HomeFeatureAccent.GOLD),
    HomeFeature(6, "تعليم القرآن", "دروس وتلاوات", Icons.Outlined.Headset, HomeFeatureAccent.GREEN),
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onSettingsClick: () -> Unit = {},
    onThemeToggleClick: () -> Unit = {},
    onFeatureClick: (HomeFeature) -> Unit = {}
) {
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
                onSettingsClick = onSettingsClick,
                onThemeToggleClick = onThemeToggleClick
            )

            // Pulls the ayah card up so it overlaps the header's rounded bottom edge.
            AyahOfTheDayCard(
                ayah = uiState.ayah,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-32).dp)
            )

            Spacer(Modifier.height(2.dp))

            FeatureGrid(
                features = uiState.features,
                onFeatureClick = onFeatureClick
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeHeaderSection(
    uiState: HomeUiState,
    onSettingsClick: () -> Unit,
    onThemeToggleClick: () -> Unit
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
                .padding(top = 12.dp, bottom = 48.dp) // extra room for the overlapping ayah card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "المصحف الشريف",
                        color = Theme.colors.onPrimary,
                        style = Theme.textStyle.title.medium,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = uiState.greeting,
                        color = Theme.colors.onPrimary.copy(alpha = 0.8f),
                        style = Theme.textStyle.label.small
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HeaderIconButton(icon = Icons.Outlined.DarkMode, onClick = onThemeToggleClick)
                    Spacer(Modifier.width(4.dp))
                    SettingsIconButton(onClick = onSettingsClick)
                }
            }

            Spacer(Modifier.height(20.dp))

            NextPrayerCard(uiState = uiState)

        }
    }
}

@Composable
private fun HeaderIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(32.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Theme.colors.onPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NextPrayerCard(uiState: HomeUiState) {
    Surface(
        color = Theme.colors.surface,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
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
                            text = "الصلاة القادمة",
                            color = TextMuted,
                            style = Theme.textStyle.label.small
                        )
                        Text(
                            text = uiState.nextPrayerName,
                            color = Theme.colors.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = uiState.currentTime,
                        color = Theme.colors.primary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
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
                        Text(
                            text = uiState.remainingLabel,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            DayProgressBar(progress = uiState.dayProgress)

            Spacer(Modifier.height(40.dp))

            MiniPrayersRow(prayers = uiState.miniPrayers)
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
                MiniPrayerIcon(icon = prayer.icon)
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

/**
 * Small pastel circular badge for the header's mini prayer-times row.
 * If PrayerIconBadge already exposes a non-"closest" pastel style, prefer reusing it
 * directly here instead of this local composable to avoid duplicated icon styling.
 */
@Composable
private fun MiniPrayerIcon(icon: PrayerIcon) {
    val bg = when (icon) {
        PrayerIcon.FAJR -> Color(0xFFDCE9E3)
        PrayerIcon.SUNRISE -> Color(0xFFFBEBD1)
        PrayerIcon.DHUHR -> Color(0xFFFBEBD1)
        PrayerIcon.ASR -> Color(0xFFFCE6D8)
        PrayerIcon.MAGHRIB -> Color(0xFFF6DCE0)
        PrayerIcon.ISHA -> Color(0xFFE1E0F2)
    }
    val tint = when (icon) {
        PrayerIcon.FAJR -> Color(0xFF2FA671)
        PrayerIcon.SUNRISE -> Color(0xFFD9A441)
        PrayerIcon.DHUHR -> Color(0xFFD9A441)
        PrayerIcon.ASR -> Color(0xFFD98B41)
        PrayerIcon.MAGHRIB -> Color(0xFFC65C7A)
        PrayerIcon.ISHA -> Color(0xFF5B58A6)
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun AyahOfTheDayCard(ayah: AyahOfDay, modifier: Modifier = Modifier) {
    Surface(
        color = AyahCardBackground,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AyahAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "آية اليوم",
                color = TextMuted,
                style = Theme.textStyle.label.small
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = ayah.text,
                color = Theme.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(
                color = AyahAccent.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth(0.4f)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = ayah.reference,
                color = TextMuted,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FeatureGrid(
    features: List<HomeFeature>,
    onFeatureClick: (HomeFeature) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        features.chunked(2).forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowFeatures.forEach { feature ->
                    FeatureCard(
                        feature = feature,
                        modifier = Modifier.weight(1f),
                        onClick = { onFeatureClick(feature) }
                    )
                }
                if (rowFeatures.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    feature: HomeFeature,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (iconBg, iconTint) = when (feature.accent) {
        HomeFeatureAccent.GOLD -> GoldAccentBg to GoldAccent
        HomeFeatureAccent.GREEN -> Theme.colors.primary.copy(alpha = 0.12f) to Theme.colors.primary
    }

    Surface(
        color = Theme.colors.surface,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = feature.title,
                color = Theme.colors.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = feature.subtitle,
                color = TextMuted,
                fontSize = 12.sp
            )
        }
    }
}

// ---------------------------------------------------------------------------------
// Preview mocks
// ---------------------------------------------------------------------------------

private fun mockHomeUiState() = HomeUiState(
    miniPrayers = listOf(
        PrayerItem(6, "العشاء", "7:45", PrayerIcon.ISHA, PrayerStatus.UPCOMING, hasNotificationToggle = false),
        PrayerItem(5, "المغرب", "6:15", PrayerIcon.MAGHRIB, PrayerStatus.UPCOMING, hasNotificationToggle = false),
        PrayerItem(4, "العصر", "3:45", PrayerIcon.ASR, PrayerStatus.UPCOMING, hasNotificationToggle = false),
        PrayerItem(2, "الشروق", "6:45", PrayerIcon.SUNRISE, PrayerStatus.PAST, hasNotificationToggle = false),
        PrayerItem(1, "الفجر", "5:15", PrayerIcon.FAJR, PrayerStatus.PAST, hasNotificationToggle = false),
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenPreview() {
    MuslimTheme {
        HomeScreen(uiState = mockHomeUiState())
    }
}