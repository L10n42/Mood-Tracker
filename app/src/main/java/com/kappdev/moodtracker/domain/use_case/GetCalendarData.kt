package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.CalendarMonth
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.end
import com.kappdev.moodtracker.domain.model.start
import com.kappdev.moodtracker.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetCalendarData @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(month: CalendarMonth): Flow<Map<LocalDate, Mood?>> {
        return repository.getMoodsFor(month.start, month.end).map { moods ->
            transformData(moods, month)
        }
    }

    private fun transformData(moods: List<Mood>, month: CalendarMonth): Map<LocalDate, Mood?> {
        val calendarData = month.days.associateWith<LocalDate, Mood?> { null }.toMutableMap()

        for (calendarDay in month.days) {
            calendarData[calendarDay] = moods.firstOrNull { it.date == calendarDay }
        }

        return calendarData
    }
}