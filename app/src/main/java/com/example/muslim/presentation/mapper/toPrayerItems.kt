package com.example.muslim.presentation.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.muslim.domain.model.Data
import com.example.muslim.presentation.screens.prayerTimes.PrayerHeaderInfo
import com.example.muslim.presentation.screens.prayerTimes.PrayerIcon
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem
import com.example.muslim.presentation.screens.prayerTimes.PrayerStatus
import com.example.muslim.util.convertENDayToAr
import com.example.muslim.util.convertENMonthToAr
import com.example.muslim.util.time12Hour

@RequiresApi(Build.VERSION_CODES.O)
fun Data.toPrayerItems(): List<PrayerItem> {
    return listOf(
        PrayerItem("fajr", "الفجر", times.Fajr.toDisplayTime(), PrayerIcon.FAJR, PrayerStatus.UPCOMING),
        PrayerItem("dhuhr", "الظهر", times.Dhuhr.toDisplayTime(), PrayerIcon.DHUHR, PrayerStatus.UPCOMING),
        PrayerItem("asr", "العصر", times.Asr.toDisplayTime(), PrayerIcon.ASR, PrayerStatus.UPCOMING),
        PrayerItem("maghrib", "المغرب", times.Maghrib.toDisplayTime(), PrayerIcon.MAGHRIB, PrayerStatus.UPCOMING),
        PrayerItem("isha", "العشاء", times.Isha.toDisplayTime(), PrayerIcon.ISHA, PrayerStatus.UPCOMING),
    )
}

fun Data.toHeaderInfo(): PrayerHeaderInfo {
    return PrayerHeaderInfo(
        locationLabel = "القاهرة،   مصر",
        dayOfWeek = date.gregorian.weekday.en.convertENDayToAr(),
        gregorianDate = "${date.gregorian.day}  ${date.gregorian.month.en.convertENMonthToAr()}  ${date.gregorian.year}",
        hijriDate = "${date.hijri.day} ${date.hijri.month.ar} ${date.hijri.year}"
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun String.toDisplayTime(): String = substringBefore(" ").time12Hour()