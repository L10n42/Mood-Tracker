package com.kappdev.moodtracker.domain.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.getMonthName(short: Boolean = false): String {
    val monthFormatter = DateTimeFormatter.ofPattern(if (short) "MMM" else "MMMM")
    return this.format(monthFormatter)
}

fun LocalDate.isToday(): Boolean {
    return (this == LocalDate.now())
}

fun LocalDate.sameMonthWith(date: LocalDate): Boolean {
    return (this.month == date.month && this.year == date.year)
}

fun LocalDate.isCurrentMonth(): Boolean {
    val now = LocalDate.now()
    return (this.month == now.month && this.year == now.year)
}