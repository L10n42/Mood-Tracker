package com.kappdev.moodtracker.domain.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ImagePathsConverter {

    private val gson = Gson()
    private val imagesType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun toImages(imageJson: String?): List<String>? {
        if (imageJson == null) {
            return null
        }
        return gson.fromJson<List<String>>(imageJson, imagesType)
    }

    @TypeConverter
    fun fromImages(images: List<String>?): String? {
        if (images == null) {
            return null
        }
        return gson.toJson(images, imagesType)
    }
}