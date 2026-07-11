package com.example.muslim.presentation.screens.prayerTimes


data class PrayerTimesUiState(
    val header: PrayerHeaderInfo? = null,
    val prayers: List<PrayerItem> = emptyList(),
    val currentDate: String = "2026-07-01",
    val currentTime: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class PrayerStatus {
    PAST,
    CURRENT,
    UPCOMING
}

data class PrayerItem(
    val id: String,
    val name: String,
    val time: String,
    val icon: PrayerIcon,
    val status: PrayerStatus = PrayerStatus.UPCOMING,
    val hasNotificationToggle: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val countdownLabel: String? = null,
    val remainingFraction: Float? = null
)

data class PrayerHeaderInfo(
    val locationLabel: String,
    val dayOfWeek: String,
    val gregorianDate: String,
    val hijriDate: String
)

enum class PrayerIcon {
    FAJR, SUNRISE, DHUHR, ASR, MAGHRIB, ISHA
}


// ---------------------------------------------------------------------
// Sample data matching the design mock.
// ---------------------------------------------------------------------

fun mockHeaderInfo() = PrayerHeaderInfo(
    locationLabel = "القاهرة، مصر",
    dayOfWeek = "الثلاثاء",
    gregorianDate = "21 يوليو 2026",
    hijriDate = "28 ربيع الثاني 1447"
)

fun mockPrayerTimes() = listOf(
    PrayerItem(
        id = "fajr",
        name = "الفجر",
        time = "5:15",
        icon = PrayerIcon.FAJR,
        status = PrayerStatus.PAST,
        isNotificationEnabled = false
    ),
    PrayerItem(
        id = "sunrise",
        name = "الشروق",
        time = "6:45",
        icon = PrayerIcon.SUNRISE,
        status = PrayerStatus.PAST,
        hasNotificationToggle = false
    ),
    PrayerItem(
        id = "dhuhr",
        name = "الظهر",
        time = "12:30",
        icon = PrayerIcon.DHUHR,
        status = PrayerStatus.CURRENT,
        isNotificationEnabled = true,
        countdownLabel = "بعد ساعة و 15 دقيقة",
        remainingFraction = 0.45f
    ),
    PrayerItem(
        id = "asr",
        name = "العصر",
        time = "3:45",
        icon = PrayerIcon.ASR,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    ),
    PrayerItem(
        id = "maghrib",
        name = "المغرب",
        time = "6:15",
        icon = PrayerIcon.MAGHRIB,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    ),
    PrayerItem(
        id = "isha",
        name = "العشاء",
        time = "7:45",
        icon = PrayerIcon.ISHA,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    )
)
