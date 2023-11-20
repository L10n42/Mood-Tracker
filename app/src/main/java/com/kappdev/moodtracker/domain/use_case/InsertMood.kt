package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.util.fail
import java.time.LocalDate
import javax.inject.Inject

class InsertMood @Inject constructor(
    private val repository: MoodRepository
) {

    suspend operator fun invoke(date: LocalDate?, moodType: MoodType?, note: String): Result<Long> {
        date ?: return Result.fail("Incorrect date")
        moodType ?: return Result.fail("Choose a mood")

        val mood = Mood(date, moodType, note)
        val result = repository.insertMood(mood)

        return if (result > 0) {
            Result.success(result)
        } else {
            Result.fail("Insert error")
        }
    }
}