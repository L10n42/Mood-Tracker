package com.kappdev.moodtracker.domain.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ImageUrisConverter {

    private val gson = Gson()
    private val imagesType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun toImages(imageJson: String?): List<Uri>? {
        if (imageJson == null) {
            return null
        }
        val result: List<String>? = gson.fromJson<List<String>>(imageJson, imagesType)
        return result?.map { Uri.parse(it) }
    }

    @TypeConverter
    fun fromImages(images: List<Uri>?): String? {
        if (images == null) {
            return null
        }
        return gson.toJson(images.map { it.toString() }, imagesType)
    }
}