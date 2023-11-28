package com.kappdev.moodtracker.presentation.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodStreak
import com.kappdev.moodtracker.domain.use_case.FindMoodStreaks
import com.kappdev.moodtracker.domain.use_case.GetCalendarData
import com.kappdev.moodtracker.domain.use_case.GetCalendarMonth
import com.kappdev.moodtracker.domain.util.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarData: GetCalendarData,
    private val toaster: Toaster
) : ViewModel() {

    var calendarState by mutableStateOf(CalendarState.IDLE)
        private set

    var calendarDate by mutableStateOf<LocalDate>(LocalDate.now())
        private set

    var calendarMonth by mutableStateOf(GetCalendarMonth().invoke(calendarDate))
        private set

    var data by mutableStateOf<Map<LocalDate, Mood?>>(emptyMap())
        private set

    var moodStreaks by mutableStateOf<List<MoodStreak>>(emptyList())
        private set

    private var calendarJob: Job? = null

    fun launch() {
        updateCalendarData()
    }

    fun changeCalendarDate(date: LocalDate) {
        calendarDate = date
        updateCalendarMonth()
        updateCalendarData()
    }

    private fun updateCalendarData() {
        calendarJob?.cancel()
        calendarJob = viewModelScope.launch(Dispatchers.IO) {
            calendarState = CalendarState.LOADING
            data = getCalendarData(calendarMonth)
            findMoodStreaks()
            calendarState = CalendarState.READY
        }
    }

    fun ifDateIsValid(date: LocalDate, block: () -> Unit) {
        when {
            (date > LocalDate.now()) -> toaster.show(R.string.invalid_future_date_msg)
            else -> block()
        }
    }

    private fun updateCalendarMonth() {
        calendarMonth = GetCalendarMonth().invoke(calendarDate)
    }

    private fun findMoodStreaks() {
        moodStreaks = FindMoodStreaks().invoke(data.values.toList())
    }
}
