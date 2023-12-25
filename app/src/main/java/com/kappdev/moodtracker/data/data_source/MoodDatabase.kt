package com.kappdev.moodtracker.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.kappdev.moodtracker.domain.converters.ImagePathsConverter
import com.kappdev.moodtracker.domain.converters.LocalDateConverter
import com.kappdev.moodtracker.domain.converters.MoodTypeConverter
import com.kappdev.moodtracker.domain.model.Mood

@Database(
    entities = [Mood::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = MoodDatabase.Migration1To2::class),
        AutoMigration (from = 2, to = 3)
    ],
    exportSchema = true
)
@TypeConverters(MoodTypeConverter::class, LocalDateConverter::class, ImagePathsConverter::class)
abstract class MoodDatabase : RoomDatabase() {

    abstract val moodDao: MoodDao

    companion object {
        const val NAME = "mood_database"
    }

    @DeleteColumn(tableName = "moods", columnName = "mood_id")
    @RenameColumn(tableName = "moods", fromColumnName = "images", toColumnName = "image_paths")
    class Migration1To2 : AutoMigrationSpec
}