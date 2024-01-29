package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodStreak

class FindMoodStreaks {

    operator fun invoke(data: List<Mood?>): List<MoodStreak> {
        val streaks = mutableListOf<MoodStreak>()
        val edges = (1 until data.size / 7).map { it * 7 - 1 }

        var streakStart = -1
        var isOpen = false

        for (i in data.indices) {
            val today = data.getOrNull(i)
            val tomorrow = data.getOrNull(i + 1)

            if (today != null && tomorrow != null && today.type == tomorrow.type) {
                if (streakStart == -1) {
                    streakStart = i
                }

                if (i in edges) {
                    val streak = MoodStreak(streakStart to i, data[streakStart]!!.type, isOpen = isOpen, isClose = true)
                    streaks.add(streak)
                    streakStart = i + 1
                    isOpen = true
                }
            } else if (streakStart != -1) {
                val streak = MoodStreak(streakStart to i, data[streakStart]!!.type, isOpen = isOpen)
                streaks.add(streak)
                streakStart = -1
                isOpen = false
            }
        }

        if (streakStart != -1 && streakStart != data.lastIndex) {
            val streak = MoodStreak(streakStart to data.lastIndex, data[streakStart]!!.type, isOpen = isOpen)
            streaks.add(streak)
        }

        return streaks
    }
}