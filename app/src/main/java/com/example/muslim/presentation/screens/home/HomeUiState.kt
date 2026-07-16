package com.example.muslim.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem

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
    HomeFeature(
        1,
        "المصحف",
        "قراءة القرآن الكريم",
        Icons.AutoMirrored.Outlined.MenuBook,
        HomeFeatureAccent.GREEN
    ),
    HomeFeature(2, "الأذان", "مواقيت الصلاة", Icons.Outlined.Schedule, HomeFeatureAccent.GOLD),
    HomeFeature(
        3,
        "الأدعية والأذكار",
        "حصن المسلم",
        Icons.Outlined.PanTool,
        HomeFeatureAccent.GREEN
    ),
    HomeFeature(
        4,
        "التذكيرات",
        "تنبيهات يومية",
        Icons.Outlined.Notifications,
        HomeFeatureAccent.GOLD
    ),

    HomeFeature(5, "تعليم القرآن", "دروس وتلاوات", Icons.Outlined.Headset, HomeFeatureAccent.GREEN),
    HomeFeature(6, "المساجد", "أقرب المساجد", Icons.Outlined.LocationOn, HomeFeatureAccent.GOLD),
)