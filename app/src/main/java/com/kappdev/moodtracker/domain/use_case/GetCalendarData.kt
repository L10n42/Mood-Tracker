package com.kappdev.moodtracker.domain.use_case

import android.util.Log
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.end
import com.kappdev.moodtracker.domain.model.start
import com.kappdev.moodtracker.domain.repository.MoodRepository
import java.time.LocalDate
import javax.inject.Inject

class GetCalendarData @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(date: LocalDate): Map<LocalDate, Mood?> {
        val calendarMonth = GetCalendarMonth().invoke(date)
        val calendarMoods = repository.getMoodsFor(calendarMonth.start, calendarMonth.end)

        val calendarData = calendarMonth.days.associateWith<LocalDate, Mood?> { null }.toMutableMap()

        for (calendarDay in calendarMonth.days) {
            val dataForDate = calendarMoods.firstOrNull { it.date == calendarDay }
            calendarData[calendarDay] = dataForDate
        }

        calendarData.forEach {
            Log.d("CALENDARDATA", "${it.key} -> ${it.value}")
        }
        return calendarData
    }
}