package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetWeekChartData @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(date: LocalDate): Flow<Map<Int, MoodType?>> {
        val firstDayOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val lastDayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return repository.getMoodsFor(firstDayOfWeek, lastDayOfWeek).map(::transformData)
    }

    private fun transformData(moods: List<Mood>): Map<Int, MoodType?> {
        val weekDays = (1..7)
        val chartData = weekDays.associateWith<Int, Mood?> { null }.toMutableMap()

        for (day in weekDays) {
            chartData[day] = moods.firstOrNull { it.date.dayOfWeek.value == day }
        }

        return chartData.mapValues { it.value?.type }
    }
}