package com.kappdev.moodtracker.presentation.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodStreak
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.use_case.FindMoodStreaks
import com.kappdev.moodtracker.domain.use_case.GetCalendarData
import com.kappdev.moodtracker.domain.use_case.GetCalendarMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarData: GetCalendarData
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
            delay(5000)
            calendarState = CalendarState.READY
        }
    }

    private fun updateCalendarMonth() {
        calendarMonth = GetCalendarMonth().invoke(calendarDate)
    }

    private fun findMoodStreaks() {
        moodStreaks = FindMoodStreaks().invoke(data.values.toList())
    }
}

private fun generateCalendarDays(currentMonth: LocalDate): List<Mood> {
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val lastDayOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth())

    val daysInMonth = mutableListOf<Mood>()

    val daysBetween = ChronoUnit.DAYS.between(firstDayOfMonth, lastDayOfMonth)

    for (i in 0..daysBetween) {
        val currentDate = firstDayOfMonth.plusDays(i)
        daysInMonth.add(Mood(date = currentDate, type = getRandomMood()))
    }

    val daysBefore = firstDayOfMonth.dayOfWeek.value - 1
    val daysAfter = 7 - lastDayOfMonth.dayOfWeek.value

    val previousMonth = currentMonth.minusMonths(1)
    val nextMonth = currentMonth.plusMonths(1)

    val daysBeforeList = (1..daysBefore).map {
        val date = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth() - daysBefore + it)
        Mood(date = date, type = getRandomMood())
    }

    val daysAfterList = (1..daysAfter).map {
        val date = nextMonth.withDayOfMonth(it)
        Mood(date = date, type = getRandomMood())
    }

    return daysBeforeList + daysInMonth + daysAfterList
}

private fun getRandomMood(): MoodType {
    return listOf(
        MoodType.Bad,
        MoodType.Awful,
        MoodType.Good,
        MoodType.Meh,
        MoodType.Rad
    ).random()
}
