package com.example.muslim.util

import com.example.muslim.data.local.database.entity.AlarmEntity
import com.example.muslim.domain.model.Times
import com.example.muslim.presentation.screens.prayerTimes.PrayerIcon
import com.example.muslim.presentation.screens.prayerTimes.PrayerItem

fun List<AlarmEntity>.getCachedPrayers(): List<PrayerItem> {
    val alarms = this
    return listOf(
        PrayerItem(
            1,
            Constants.FAJR,
            "${alarms[0].hour}:${alarms[0].minute}",
            PrayerIcon.FAJR,
            isEnable = alarms.getOrNull(0)?.isEnabled ?: true
        ),
        PrayerItem(
            2,
            Constants.DHUHR,
            "${alarms[1].hour}:${alarms[1].minute}",
            PrayerIcon.DHUHR,
            isEnable = alarms.getOrNull(1)?.isEnabled ?: true,
        ),
        PrayerItem(
            3,
            Constants.ASR,
            "${alarms[2].hour}:${alarms[2].minute}",
            PrayerIcon.ASR,
            isEnable = alarms.getOrNull(2)?.isEnabled ?: true,
        ),
        PrayerItem(
            4,
            Constants.MAGHRIB,
            "${alarms[3].hour}:${alarms[3].minute}",
            PrayerIcon.MAGHRIB,
            isEnable = alarms.getOrNull(3)?.isEnabled ?: true,
        ),
        PrayerItem(
            5,
            Constants.ISHA,
            "${alarms[4].hour}:${alarms[4].minute}",
            PrayerIcon.ISHA,
            isEnable = alarms.getOrNull(4)?.isEnabled ?: true,
        ),
    )
}


fun List<String>.toTimes(): Times {
    return Times(
        Fajr = this[0],
        Dhuhr = this[1],
        Asr = this[2],
        Maghrib = this[3],
        Isha = this[4],
        Firstthird = "",
        Imsak = "",
        Lastthird = "",
        Midnight = "",
        Sunrise = "",
        Sunset = "",
    )
}

