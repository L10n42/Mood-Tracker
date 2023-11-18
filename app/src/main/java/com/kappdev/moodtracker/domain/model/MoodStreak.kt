package com.kappdev.moodtracker.domain.model

data class MoodStreak(
    val range: Pair<Int, Int>,
    val type: MoodType
)

val MoodStreak.size: Int
    get() = this.range.second - this.range.first + 1