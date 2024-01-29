package com.kappdev.moodtracker.domain.model

data class MoodStreak(
    val range: Pair<Int, Int>,
    val moodType: MoodType,
    val isOpen: Boolean = false,
    val isClose: Boolean = false
)

val MoodStreak.size: Int
    get() = (this.range.second - this.range.first) + 1