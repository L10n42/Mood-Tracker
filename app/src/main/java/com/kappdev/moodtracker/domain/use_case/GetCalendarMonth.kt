package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.CalendarMonth
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GetCalendarMonth {

    operator fun invoke(date: LocalDate): CalendarMonth {
        val firstDayOfMonth = date.withDayOfMonth(1)
        val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())

        val daysInMonth = mutableListOf<LocalDate>()

        val daysBetween = ChronoUnit.DAYS.between(firstDayOfMonth, lastDayOfMonth)

        for (i in 0..daysBetween) {
            daysInMonth.add(firstDayOfMonth.plusDays(i))
        }

        val daysBefore = firstDayOfMonth.dayOfWeek.value - 1
        val daysAfter = 7 - lastDayOfMonth.dayOfWeek.value

        val previousMonth = date.minusMonths(1)
        val nextMonth = date.plusMonths(1)

        val daysBeforeList = (1..daysBefore).map {
            previousMonth.withDayOfMonth(previousMonth.lengthOfMonth() - daysBefore + it)
        }

        val daysAfterList = (1..daysAfter).map {
            nextMonth.withDayOfMonth(it)
        }

        return CalendarMonth(daysBeforeList + daysInMonth + daysAfterList)
    }
}