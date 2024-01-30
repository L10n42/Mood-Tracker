package com.kappdev.moodtracker.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kappdev.moodtracker.domain.model.Mood
import java.time.LocalDate

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: Mood): Long

    @Query("SELECT * FROM moods WHERE date=:date")
    fun getMoodByDate(date: LocalDate): Mood?

    @Query("SELECT * FROM moods WHERE date BETWEEN :start AND :end")
    fun getMoodsFor(start: LocalDate, end: LocalDate): List<Mood>

    @Query("DELETE FROM moods WHERE date = :date")
    fun deleteByDate(date: LocalDate): Int

}