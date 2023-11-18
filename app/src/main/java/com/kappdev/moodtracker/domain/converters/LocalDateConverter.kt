package com.kappdev.moodtracker.domain.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }
}