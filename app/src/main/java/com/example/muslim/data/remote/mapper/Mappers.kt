package com.example.muslim.data.remote.mapper

import com.example.muslim.data.remote.dto.*
import com.example.muslim.domain.model.*

fun PrayerTimesForMonthDTO.toDomain(): PrayerTimesForMonth {
    return PrayerTimesForMonth(
        code = code ?: 0,
        data = data?.map { it.toDomain() } ?: emptyList(),
        qibla = qibla?.toDomain() ?: Qibla(Direction(false, 0.0, ""), Distance("", 0.0)),
        status = status ?: "",
        timezone = timezone?.toDomain() ?: Timezone("", "", "")
    )
}

fun DataDTO.toDomain(): Data {
    return Data(
        date = date,
        hijri_date = hijriDate.toDomain(),
        prohibited_times = prohibitedTimes.toDomain(),
        times = times.toDomain()
    )
}

fun HijriDateDTO.toDomain(): HijriDate {
    return HijriDate(
        gregorian = gregorian.toDomain(),
        hijri = hijri.toDomain(),
        readable = readable,
        timestamp = timestamp
    )
}

fun GregorianDTO.toDomain(): Gregorian {
    return Gregorian(
        date = date,
        day = day,
        designation = designation.toDomain(),
        format = format,
        month = month.toDomain(),
        weekday = weekday.toDomain(),
        year = year
    )
}

fun HijriDTO.toDomain(): Hijri {
    return Hijri(
        adjustedHolidays = adjustedHolidays,
        date = date,
        day = day,
        designation = designation.toDomain(),
        format = format,
        holidays = holidays,
        method = method,
        month = month.toDomain(),
        shift = shift,
        weekday = weekday.toDomain(),
        year = year
    )
}

fun DesignationDTO.toDomain(): Designation {
    return Designation(
        abbreviated = abbreviated,
        expanded = expanded
    )
}

fun MonthDTO.toDomain(): Month {
    return Month(
        en = en,
        number = number
    )
}

fun MonthXDTO.toDomain(): MonthX {
    return MonthX(
        ar = ar,
        days = days,
        en = en,
        number = number
    )
}

fun WeekdayDTO.toDomain(): Weekday {
    return Weekday(
        en = en
    )
}

fun WeekdayXDTO.toDomain(): WeekdayX {
    return WeekdayX(
        ar = ar,
        en = en
    )
}

fun ProhibitedTimesDTO.toDomain(): ProhibitedTimes {
    return ProhibitedTimes(
        noon = noon.toDomain(),
        sunrise = sunrise.toDomain(),
        sunset = sunset.toDomain()
    )
}

fun NoonDTO.toDomain(): Noon {
    return Noon(
        end = end,
        start = start
    )
}

fun SunriseDTO.toDomain(): Sunrise {
    return Sunrise(
        end = end,
        start = start
    )
}

fun SunsetDTO.toDomain(): Sunset {
    return Sunset(
        end = end,
        start = start
    )
}

fun TimesDTO.toDomain(): Times {
    return Times(
        Asr = asr,
        Dhuhr = dhuhr,
        Fajr = fajr,
        Firstthird = firstthird,
        Imsak = imsak,
        Isha = isha,
        Lastthird = lastthird,
        Maghrib = maghrib,
        Midnight = midnight,
        Sunrise = sunrise,
        Sunset = sunset
    )
}

fun QiblaDTO.toDomain(): Qibla {
    return Qibla(
        direction = direction.toDomain(),
        distance = distance.toDomain()
    )
}

fun DirectionDTO.toDomain(): Direction {
    return Direction(
        clockwise = clockwise,
        degrees = degrees,
        from = from
    )
}

fun DistanceDTO.toDomain(): Distance {
    return Distance(
        unit = unit,
        value = value
    )
}

fun TimezoneDTO.toDomain(): Timezone {
    return Timezone(
        abbreviation = abbreviation,
        name = name,
        utc_offset = utcOffset
    )
}
