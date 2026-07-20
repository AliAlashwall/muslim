package com.example.muslim.presentation.screens.prayerTimes

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.muslim.util.convertENDayToAr
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale


data class PrayerTimesUiState(
    val header: PrayerHeaderInfo? = null,
    val prayers: List<PrayerItem> = emptyList(),
    val currentDate: String = "2026-07-01",
    val currentTime: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFajrNotifEnable: Boolean = true,
    val isDhuhrNotifEnable: Boolean = true,
    val isAsrNotifEnable: Boolean = true,
    val isMaghribNotifEnable: Boolean = true,
    val isIshaNotifEnable: Boolean = true,
    val fajrStatus: PrayerStatus = PrayerStatus.UPCOMING,
    val dhuhrStatus: PrayerStatus = PrayerStatus.UPCOMING,
    val asrStatus: PrayerStatus = PrayerStatus.UPCOMING,
    val maghribStatus: PrayerStatus = PrayerStatus.UPCOMING,
    val ishaStatus: PrayerStatus = PrayerStatus.UPCOMING,
    val remainingTime: String? = null,
)

enum class PrayerStatus {
    PAST,
    CLOSEST,
    UPCOMING
}

data class PrayerItem(
    val id: Int,
    val name: String,
    val time: String,
    val icon: PrayerIcon,
    val status: PrayerStatus = PrayerStatus.UPCOMING,
    val hasNotificationToggle: Boolean = true,
    val remainingMinutesValue: Int? = null,
    val isEnable: Boolean? = null
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

@RequiresApi(Build.VERSION_CODES.O)
fun getCachedHeaderInfo(): PrayerHeaderInfo {
    val locale = Locale("ar")

    val gregorianFormatter =
        DateTimeFormatter.ofPattern("d MMMM yyyy", locale)

    val hijriFormatter =
        DateTimeFormatter.ofPattern("d MMMM yyyy", locale)

    val gregorian = LocalDate.now().format(gregorianFormatter)
    val hijri = HijrahDate.now().format(hijriFormatter)
    return PrayerHeaderInfo(
        locationLabel = "طنطا، مصر",
        dayOfWeek = LocalDate.now().dayOfWeek.name.convertENDayToAr(),
        gregorianDate = gregorian,
        hijriDate = hijri
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun mockHeaderInfo() = getCachedHeaderInfo()

