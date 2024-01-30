package com.kappdev.moodtracker.domain.repository

import com.kappdev.moodtracker.domain.model.Mood
import java.time.LocalDate

interface MoodRepository {

    suspend fun insertMood(mood: Mood): Long

    fun getMoodByDate(date: LocalDate): Mood?

    fun getMoodsFor(start: LocalDate, end: LocalDate): List<Mood>

    fun deleteByDate(date: LocalDate): Int
}