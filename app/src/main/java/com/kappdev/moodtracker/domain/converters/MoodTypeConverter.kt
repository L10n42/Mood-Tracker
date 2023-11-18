package com.kappdev.moodtracker.domain.converters

import androidx.room.TypeConverter
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.model.fromKey

object MoodTypeConverter {

    @TypeConverter
    fun toMoodType(typeKey: String): MoodType {
        return MoodType.fromKey(typeKey)
    }

    @TypeConverter
    fun fromMoodType(moodType: MoodType): String {
        return moodType.key
    }
}