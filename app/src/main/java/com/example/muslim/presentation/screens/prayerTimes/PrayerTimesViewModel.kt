package com.example.muslim.presentation.screens.prayerTimes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muslim.data.local.database.dao.AlarmDao
import com.example.muslim.data.local.database.entity.AlarmEntity
import com.example.muslim.data.scheduler.AlarmScheduler
import com.example.muslim.domain.model.Times
import com.example.muslim.domain.usecase.GetPrayerTimesUseCase
import com.example.muslim.domain.util.APIResult
import com.example.muslim.presentation.mapper.get24Hours
import com.example.muslim.presentation.mapper.getMinutes
import com.example.muslim.presentation.mapper.toHeaderInfo
import com.example.muslim.presentation.mapper.toPrayerItems
import com.example.muslim.util.Constants
import com.example.muslim.util.getCachedPrayers
import com.example.muslim.util.getTimeDuration
import com.example.muslim.util.toTimes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
    private val dao: AlarmDao,
    private val scheduler: AlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerTimesUiState())
    val uiState: StateFlow<PrayerTimesUiState> = _uiState.asStateFlow()

    init {
        getCurrentDateTime()
        loadData()
        refreshPrayerNotif()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDateTime() {
        val now = LocalDateTime.now()
        _uiState.update {
            it.copy(
                currentDate = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                currentTime = now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val currentDate = _uiState.value.currentDate
            _uiState.update { it.copy(isLoading = true) }

            val cachedAlarms = dao.getAll().first()
            if (cachedAlarms.isNotEmpty() && cachedAlarms[0].date == currentDate) {
                processCachedData(cachedAlarms, currentDate)
            } else {
                fetchFromNetwork(currentDate)
            }
        }
    }

    private fun processCachedData(cachedAlarms: List<AlarmEntity>, currentDate: String) {
        val prayers = cachedAlarms.getCachedPrayers()
        val times = prayers.map { it.time }.toTimes()
        val statuses = calculatePrayerStatuses(times, currentDate)

        val prayerItems = prayers.mapIndexed { index, item ->
            item.copy(status = statuses[index])
        }

        _uiState.update { state ->
            state.copy(
                prayers = prayerItems,
                header = getCachedHeaderInfo(),
                isLoading = false,
                // Update specific booleans from the list for legacy UI support if needed
                isFajrNotifEnable = prayerItems.find { it.name == Constants.FAJR }?.isEnable
                    ?: true,
                isDhuhrNotifEnable = prayerItems.find { it.name == Constants.DHUHR }?.isEnable
                    ?: true,
                isAsrNotifEnable = prayerItems.find { it.name == Constants.ASR }?.isEnable ?: true,
                isMaghribNotifEnable = prayerItems.find { it.name == Constants.MAGHRIB }?.isEnable
                    ?: true,
                isIshaNotifEnable = prayerItems.find { it.name == Constants.ISHA }?.isEnable ?: true
            )
        }
    }

    private suspend fun fetchFromNetwork(currentDate: String) {
        when (val result = getPrayerTimesUseCase(currentDate)) {
            is APIResult.Success -> {
                val todayData = result.data.data ?: return
                val statuses = calculatePrayerStatuses(todayData.times, currentDate)
                val prayerItems = todayData.toPrayerItems().mapIndexed { index, item ->
                    item.copy(status = statuses[index])
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        header = todayData.toHeaderInfo(),
                        prayers = prayerItems,
                        error = null
                    )
                }

                // Save to DB in one batch if possible, or sequentially in one coroutine
                savePrayersToDb(todayData.times)
            }
            is APIResult.Error -> {
                _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
            else -> { _uiState.update { it.copy(isLoading = false) } }
        }
    }


    private fun savePrayersToDb(times: Times) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAlarms(
                1,
                times.Fajr.get24Hours(),
                times.Fajr.getMinutes(),
                Constants.FAJR,
                _uiState.value.isFajrNotifEnable
            )

            updateAlarms(
                2,
                times.Dhuhr.get24Hours(),
                times.Dhuhr.getMinutes(),
                Constants.DHUHR,
                _uiState.value.isDhuhrNotifEnable
            )
            updateAlarms(
                3,
                times.Asr.get24Hours(),
                times.Asr.getMinutes(),
                Constants.ASR,
                _uiState.value.isAsrNotifEnable
            )
            updateAlarms(
                4,
                times.Maghrib.get24Hours(),
                times.Maghrib.getMinutes(),
                Constants.MAGHRIB,
                _uiState.value.isMaghribNotifEnable
            )
            updateAlarms(
                5,
                times.Isha.get24Hours(),
                times.Isha.getMinutes(),
                Constants.ISHA,
                _uiState.value.isIshaNotifEnable
            )
        }
    }

    fun updateRemainingTime(time: String) {
        getCurrentDateTime()
        val duration = getTimeDuration(
            _uiState.value.currentTime,
            time
        )
        _uiState.update { it.copy(remainingTime = duration) }
    }


    private fun updateAlarms(id: Int, hour: Int, minute: Int, label: String, isEnabled: Boolean) {
        viewModelScope.launch {
            dao.upsert(
                AlarmEntity(
                    id = id,
                    date = _uiState.value.currentDate,
                    hour = hour,
                    minute = minute,
                    label = label,
                    isEnabled = isEnabled
                )
            ).toInt()
            if (isEnabled) {
                scheduler.schedule(
                    AlarmEntity(
                        id = id,
                        date = _uiState.value.currentDate,
                        hour = hour,
                        minute = minute,
                        label = label,
                        isEnabled = true
                    )
                )
            } else {
                scheduler.cancel(
                    AlarmEntity(
                        id = id,
                        date = _uiState.value.currentDate,
                        hour = hour,
                        minute = minute,
                        label = label,
                        isEnabled = false
                    )
                )
            }
        }
    }

    private fun refreshPrayerNotif() {
        viewModelScope.launch {
            dao.getAll().collect { alarms ->
                //As alarms are sorted from 0 to 4,
                // so we can avoid unnecessary calculate
                // and only call each one by alarms[]
                _uiState.update {
                    it.copy(
                        isFajrNotifEnable = alarms.getOrNull(0)?.isEnabled ?: true,
                        isDhuhrNotifEnable = alarms.getOrNull(1)?.isEnabled ?: true,
                        isAsrNotifEnable = alarms.getOrNull(2)?.isEnabled ?: true,
                        isMaghribNotifEnable = alarms.getOrNull(3)?.isEnabled ?: true,
                        isIshaNotifEnable = alarms.getOrNull(4)?.isEnabled ?: true
                    )
                }
            }
        }
    }

    fun getPrayerNotifState(prayerName: String): Boolean {
        return when (prayerName) {
            Constants.FAJR -> _uiState.value.isFajrNotifEnable
            Constants.DHUHR -> _uiState.value.isDhuhrNotifEnable
            Constants.ASR -> _uiState.value.isAsrNotifEnable
            Constants.MAGHRIB -> _uiState.value.isMaghribNotifEnable
            Constants.ISHA -> _uiState.value.isIshaNotifEnable
            else -> false
        }
    }

    fun toggleAlarm(alarm: AlarmEntity, enabled: Boolean) {
        //update uiState
        when (alarm.label) {
            Constants.FAJR -> _uiState.update { it.copy(isFajrNotifEnable = enabled) }
            Constants.DHUHR -> _uiState.update { it.copy(isDhuhrNotifEnable = enabled) }
            Constants.ASR -> _uiState.update { it.copy(isAsrNotifEnable = enabled) }
            Constants.MAGHRIB -> _uiState.update { it.copy(isMaghribNotifEnable = enabled) }
            Constants.ISHA -> _uiState.update { it.copy(isIshaNotifEnable = enabled) }
        }
        // update database and scheduler
        viewModelScope.launch {
            val updated = alarm.copy(isEnabled = enabled, date = _uiState.value.currentDate)
            dao.upsert(updated)
            if (enabled) scheduler.schedule(updated) else scheduler.cancel(updated)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculatePrayerStatuses(times: Times, dateStr: String): List<PrayerStatus> {
        val now = LocalDateTime.now()
        val todayDate = now.toLocalDate()
        val targetDate = try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: Exception) {
            Log.d("PrayerTimesViewModel", "Error parsing date: ${e.message}")
            todayDate
        }
        val currentTime = now.toLocalTime()

        val prayerTimes = listOf(
            LocalTime.of(times.Fajr.get24Hours(), times.Fajr.getMinutes()),
            LocalTime.of(times.Dhuhr.get24Hours(), times.Dhuhr.getMinutes()),
            LocalTime.of(times.Asr.get24Hours(), times.Asr.getMinutes()),
            LocalTime.of(times.Maghrib.get24Hours(), times.Maghrib.getMinutes()),
            LocalTime.of(times.Isha.get24Hours(), times.Isha.getMinutes())
        )

        return when {
            targetDate.isBefore(todayDate) -> List(prayerTimes.size) { PrayerStatus.PAST }
            targetDate.isAfter(todayDate) -> List(prayerTimes.size) { PrayerStatus.UPCOMING }
            else -> {
                val nextPrayerIndex = prayerTimes.indexOfFirst { !it.isBefore(currentTime) }
                prayerTimes.indices.map { i ->
                    when {
                        nextPrayerIndex == -1 -> PrayerStatus.PAST
                        i < nextPrayerIndex -> PrayerStatus.PAST
                        i == nextPrayerIndex -> PrayerStatus.CLOSEST
                        else -> PrayerStatus.UPCOMING
                    }
                }
            }
        }
    }


}
