package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.repository.MoodRepository
import java.time.LocalDate
import javax.inject.Inject

class GetMoodByDate @Inject constructor(
    private val repository: MoodRepository
) {

    operator fun invoke(date: LocalDate): Mood? {
        return repository.getMoodByDate(date)
    }
}