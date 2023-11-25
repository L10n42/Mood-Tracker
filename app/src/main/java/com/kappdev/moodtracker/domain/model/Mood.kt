package com.kappdev.moodtracker.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappdev.moodtracker.domain.converters.ImagePathsConverter
import com.kappdev.moodtracker.domain.converters.LocalDateConverter
import com.kappdev.moodtracker.domain.converters.MoodTypeConverter
import java.time.LocalDate

@Entity(tableName = "moods")
data class Mood(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "date")
    @TypeConverters(LocalDateConverter::class)
    val date: LocalDate,

    @ColumnInfo(name = "mood_type")
    @TypeConverters(MoodTypeConverter::class)
    val type: MoodType,

    @ColumnInfo(name = "note")
    val note: String = "",

    @ColumnInfo(name = "image_paths")
    @TypeConverters(ImagePathsConverter::class)
    val images: List<String>? = null
)
