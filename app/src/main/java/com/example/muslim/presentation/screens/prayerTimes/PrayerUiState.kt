package com.example.muslim.presentation.screens.prayerTimes


/**
 * Where a prayer currently stands relative to "now".
 * Compute this (or have your API return it) by comparing the current
 * time against each prayer's start time for today.
 */
enum class PrayerStatus {
    PAST,
    CURRENT,
    UPCOMING
}

/**
 * Which badge icon to show for a prayer. Kept as a plain enum so the UI
 * layer decides how to render it (emoji today, real drawables/Lottie later)
 * - see `PrayerIcon.toEmoji()` in PrayerTimeScreen.kt.
 */
enum class PrayerIcon {
    FAJR, SUNRISE, DHUHR, ASR, MAGHRIB, ISHA
}

/**
 * One row in the prayer list. Map your API response into this shape -
 * the screen itself has no knowledge of where the data came from.
 *
 * @param id                    stable key, e.g. "fajr" - also passed back on toggle
 * @param name                  localized display name, e.g. "الفجر"
 * @param time                  pre-formatted display time, e.g. "5:15"
 * @param status                PAST / CURRENT / UPCOMING - drives the card styling
 * @param hasNotificationToggle false for rows with no reminder toggle (e.g. sunrise)
 * @param isNotificationEnabled current toggle state
 * @param countdownLabel        only shown for the CURRENT prayer, e.g. "بعد ساعة و 15 دقيقة"
 * @param remainingFraction     only used for the CURRENT prayer, 0f..1f, drives the progress bar
 */
data class PrayerUiState(
    val id: String = "1",
    val name: String = "",
    val time: String = "",
    val icon: PrayerIcon = PrayerIcon.FAJR,
    val status: PrayerStatus = PrayerStatus.UPCOMING,
    val hasNotificationToggle: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val countdownLabel: String? = null,
    val remainingFraction: Float? = null
)

/** Header content: current location + Gregorian/Hijri date. */


// ---------------------------------------------------------------------
// Sample data matching the design mock. Delete this section once you're
// mapping real responses from your prayer-times API into the models above.
// ---------------------------------------------------------------------

fun sampleHeaderInfo() = PrayerHeaderInfo(
    locationLabel = "الرياض، المملكة العربية السعودية",
    dayOfWeek = "الثلاثاء",
    gregorianDate = "21 أكتوبر 2025",
    hijriDate = "28 ربيع الثاني 1447"
)

fun samplePrayerTimes() = listOf(
    PrayerUiState(
        id = "fajr",
        name = "الفجر",
        time = "5:15",
        icon = PrayerIcon.FAJR,
        status = PrayerStatus.PAST,
        isNotificationEnabled = false
    ),
    PrayerUiState(
        id = "sunrise",
        name = "الشروق",
        time = "6:45",
        icon = PrayerIcon.SUNRISE,
        status = PrayerStatus.PAST,
        hasNotificationToggle = false
    ),
    PrayerUiState(
        id = "dhuhr",
        name = "الظهر",
        time = "12:30",
        icon = PrayerIcon.DHUHR,
        status = PrayerStatus.CURRENT,
        isNotificationEnabled = true,
        countdownLabel = "بعد ساعة و 15 دقيقة",
        remainingFraction = 0.45f
    ),
    PrayerUiState(
        id = "asr",
        name = "العصر",
        time = "3:45",
        icon = PrayerIcon.ASR,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    ),
    PrayerUiState(
        id = "maghrib",
        name = "المغرب",
        time = "6:15",
        icon = PrayerIcon.MAGHRIB,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    ),
    PrayerUiState(
        id = "isha",
        name = "العشاء",
        time = "7:45",
        icon = PrayerIcon.ISHA,
        status = PrayerStatus.UPCOMING,
        isNotificationEnabled = true
    )
)


data class PrayerHeaderInfo(
    val locationLabel: String,
    val dayOfWeek: String,
    val gregorianDate: String,
    val hijriDate: String
)