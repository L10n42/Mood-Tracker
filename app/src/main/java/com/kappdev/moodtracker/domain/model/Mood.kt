package com.kappdev.moodtracker.domain.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappdev.moodtracker.domain.converters.ImageUrisConverter
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

    @ColumnInfo(name = "images")
    @TypeConverters(ImageUrisConverter::class)
    val images: List<Uri>? = null
)
