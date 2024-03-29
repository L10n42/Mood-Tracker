package com.kappdev.moodtracker.data.repository

import com.kappdev.moodtracker.data.data_source.MoodDao
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao
) : MoodRepository {

    override suspend fun insertMood(mood: Mood): Long {
        return moodDao.insertMood(mood)
    }

    override fun getMoodByDate(date: LocalDate): Mood? {
        return moodDao.getMoodByDate(date)
    }

    override fun getMoodsFor(start: LocalDate, end: LocalDate): Flow<List<Mood>> {
        return moodDao.getMoodsFor(start, end)
    }

    override fun deleteByDate(date: LocalDate): Int {
        return moodDao.deleteByDate(date)
    }

}