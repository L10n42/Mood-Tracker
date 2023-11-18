package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodStreak

class FindMoodStreaks {

    operator fun invoke(data: List<Mood?>): List<MoodStreak> {
        val streaks = mutableListOf<MoodStreak>()

        for (start in data.indices step 7) {
            val end = minOf(start + 7, data.size)

            var streakStart = -1
            for (i in start until end - 1) {
                val today = data[i]
                val tomorrow = data[i + 1]
                if (today != null && tomorrow != null && today.type == tomorrow.type) {
                    if (streakStart == -1) {
                        streakStart = i
                    }
                } else if (streakStart != -1) {
                    val streak = MoodStreak(streakStart to i, data[streakStart]!!.type)
                    streaks.add(streak)
                    streakStart = -1
                }
            }

            if (streakStart != -1 && streakStart != end - 1) {
                val streak = MoodStreak(streakStart to end - 1, data[streakStart]!!.type)
                streaks.add(streak)
            }
        }

        return streaks
    }
}