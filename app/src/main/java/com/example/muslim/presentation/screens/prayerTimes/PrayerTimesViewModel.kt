package com.example.muslim.presentation.screens.prayerTimes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muslim.domain.repository.PrayerTimesRepository
import com.example.muslim.domain.util.APIResult
import com.example.muslim.util.convertENDayToAr
import com.example.muslim.util.convertENMonthToAr
import com.example.muslim.util.time12Hour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    private val prayerTimesRepository: PrayerTimesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerTimesUiState())
    val uiState: StateFlow<PrayerTimesUiState> = _uiState.asStateFlow()



    @RequiresApi(Build.VERSION_CODES.O)
    fun getPrayerTimes(currentDate: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = prayerTimesRepository.getPrayerTimesCurrentDay(currentDate)) {
                is APIResult.Success -> {
                    val todayData = result.data.data
                    if (todayData != null) {
                        val prayers = listOf(
                            PrayerItem(
                                id = "fajr",
                                name = "الفجر",
                                time = todayData.times.Fajr.substringBefore(" "),
                                icon = PrayerIcon.FAJR,
                                status = PrayerStatus.UPCOMING
                            ),
                            PrayerItem(
                                id = "dhuhr",
                                name = "الظهر",
                                time = todayData.times.Dhuhr.substringBefore(" ").time12Hour(),
                                icon = PrayerIcon.DHUHR,
                                status = PrayerStatus.UPCOMING
                            ),
                            PrayerItem(
                                id = "asr",
                                name = "العصر",
                                time = todayData.times.Asr.substringBefore(" ").time12Hour(),
                                icon = PrayerIcon.ASR,
                                status = PrayerStatus.UPCOMING
                            ),
                            PrayerItem(
                                id = "maghrib",
                                name = "المغرب",
                                time = todayData.times.Maghrib.substringBefore(" ").time12Hour(),
                                icon = PrayerIcon.MAGHRIB,
                                status = PrayerStatus.UPCOMING
                            ),
                            PrayerItem(
                                id = "isha",
                                name = "العشاء",
                                time = todayData.times.Isha.substringBefore(" ").time12Hour(),
                                icon = PrayerIcon.ISHA,
                                status = PrayerStatus.UPCOMING
                            )
                        )

                        val header = PrayerHeaderInfo(
                            locationLabel = "القاهرة، مصر",
                            dayOfWeek = todayData.date.gregorian.weekday.en.convertENDayToAr(),
                            gregorianDate = "${todayData.date.gregorian.day}  ${todayData.date.gregorian.month.en.convertENMonthToAr()}  ${todayData.date.gregorian.year}",
                            hijriDate = "${todayData.date.hijri.day} ${todayData.date.hijri.month.ar} ${todayData.date.hijri.year}"
                        )

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                header = header,
                                prayers = prayers,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = "No data found") }
                    }
                }

                is APIResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                else -> {}
            }
        }
    }
}
