package com.kappdev.moodtracker.domain.model

import java.time.LocalDate

data class CalendarMonth(
    val days: List<LocalDate>
)

val CalendarMonth.start: LocalDate
    get() = this.days.first()

val CalendarMonth.end: LocalDate
    get() = this.days.last()