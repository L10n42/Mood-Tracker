package com.kappdev.moodtracker.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kappdev.moodtracker.domain.converters.ImagePathsConverter
import com.kappdev.moodtracker.domain.converters.LocalDateConverter
import com.kappdev.moodtracker.domain.converters.MoodTypeConverter
import com.kappdev.moodtracker.domain.model.Mood

@Database(
    entities = [Mood::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(MoodTypeConverter::class, LocalDateConverter::class, ImagePathsConverter::class)
abstract class MoodDatabase : RoomDatabase() {

    abstract val moodDao: MoodDao

    companion object {
        const val NAME = "mood_database"
    }
}