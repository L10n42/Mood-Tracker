package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.MoodRepository
import java.time.LocalDate
import javax.inject.Inject

class GetMonthChartData @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(date: LocalDate): Map<Int, MoodType?> {
        val firstMonthDay = date.withDayOfMonth(1)
        val lastMonthDay = date.withDayOfMonth(date.lengthOfMonth())
        val monthDays = (1..date.lengthOfMonth())

        val moods = repository.getMoodsFor(firstMonthDay, lastMonthDay)

        val chartData = monthDays.associateWith<Int, Mood?> { null }.toMutableMap()

        for (day in monthDays) {
            chartData[day] = moods.firstOrNull { it.date.dayOfMonth == day }
        }

        return chartData.mapValues { it.value?.type }
    }
}