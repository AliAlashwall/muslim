package com.example.muslim.presentation.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.muslim.domain.model.Data
import com.example.muslim.presentation.screens.prayerTimes.PrayerHeaderInfo
import com.example.muslim.presentation.screens.prayerTimes.PrayerIcon
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem
import com.example.muslim.util.Constants
import com.example.muslim.util.convertENDayToAr
import com.example.muslim.util.convertENMonthToAr
import com.example.muslim.util.time12Hour
import com.example.muslim.util.toLocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun Data.toPrayerItems(): List<PrayerItem> {

    return listOf(
        PrayerItem(
            1,
            Constants.FAJR,
            time = times.Fajr.toLocalTime().minusMinutes(9).toString(),
            PrayerIcon.FAJR,
        ),
        PrayerItem(
            2,
            Constants.DHUHR,
            time = times.Dhuhr.toLocalTime().plusMinutes(1).toString(),
            PrayerIcon.DHUHR,
        ),
        PrayerItem(
            3,
            Constants.ASR,
            times.Asr.toLocalTime().minusMinutes(1).toString(),
            PrayerIcon.ASR,
        ),
        PrayerItem(
            4,
            Constants.MAGHRIB,
            times.Maghrib,
            PrayerIcon.MAGHRIB,
        ),
        PrayerItem(
            5,
            Constants.ISHA,
            times.Isha.toLocalTime().plusMinutes(3).toString(),
            PrayerIcon.ISHA,
        ),
    )
}

fun String.toPrayerAr(): String {
    return when (this) {
        Constants.FAJR -> "الفجر"

        Constants.DHUHR -> "الظهر"

        Constants.ASR -> "العصر"

        Constants.MAGHRIB -> "المغرب"

        Constants.ISHA -> "العشاء"

        else -> ""
    }
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
fun String.toDisplayTime(): String = substringBefore(" ").time12Hour()

fun String.get24Hours(): Int = substringBefore(":").toInt()
fun String.getMinutes(): Int = substringAfter(":").toInt()
