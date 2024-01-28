package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.MoodRepository
import java.time.LocalDate
import javax.inject.Inject

class QuickInsertMood @Inject constructor(
    private val repository: MoodRepository
) {

    suspend operator fun invoke(moodType: MoodType) {
        val mood = Mood(date = LocalDate.now(), type = moodType)
        repository.insertMood(mood)
    }
}