package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.CalendarMonth
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.end
import com.kappdev.moodtracker.domain.model.start
import com.kappdev.moodtracker.domain.repository.MoodRepository
import java.time.LocalDate
import javax.inject.Inject

class GetCalendarData @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(month: CalendarMonth): Map<LocalDate, Mood?> {
        val calendarMoods = repository.getMoodsFor(month.start, month.end)

        val calendarData = month.days.associateWith<LocalDate, Mood?> { null }.toMutableMap()

        for (calendarDay in month.days) {
            val dataForDate = calendarMoods.firstOrNull { it.date == calendarDay }
            calendarData[calendarDay] = dataForDate
        }

        return calendarData
    }
}