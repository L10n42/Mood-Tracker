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

fun LocalDate.isNextMonthAfter(date: LocalDate): Boolean {
    return (this.year == date.year && this.month == date.month + 1)
}

fun LocalDate.sameMonthWith(date: LocalDate): Boolean {
    return (this.month == date.month && this.year == date.year)
}

fun LocalDate.isCurrentYear(): Boolean {
    return (this.year == LocalDate.now().year)
}

fun LocalDate.plusMonth() = this.plusMonths(1)
fun LocalDate.minusMonth() = this.minusMonths(1)

fun LocalDate.plusWeek() = this.plusWeeks(1)
fun LocalDate.minusWeek() = this.minusWeeks(1)

fun LocalDate.nextMonthEnabled(): Boolean {
    val now = LocalDate.now()
    return this.year < now.year || (this.year == now.year && this.month < now.month)
}