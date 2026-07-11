package com.example.muslim.presentation.screens.prayerTimes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muslim.domain.usecase.GetPrayerTimesUseCase
import com.example.muslim.domain.util.APIResult
import com.example.muslim.presentation.mapper.toHeaderInfo
import com.example.muslim.presentation.mapper.toPrayerItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerTimesUiState())
    val uiState: StateFlow<PrayerTimesUiState> = _uiState.asStateFlow()

    init {
        getCurrentDateTime()

        getPrayerTimes(_uiState.value.currentDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDateTime() {
        val currentDateTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ?: "2026-07-1 04:30"
        _uiState.update {
            it.copy(
                currentDate = currentDateTime.substringBefore(" "),
                currentTime = currentDateTime.substringAfter(" ")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPrayerTimes(currentDate: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getPrayerTimesUseCase.invoke(currentDate)) {
                is APIResult.Success -> {
                    val todayData = result.data.data
                    if (todayData != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                header = todayData.toHeaderInfo(),
                                prayers = todayData.toPrayerItems(),
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
