package com.kappdev.moodtracker.domain.model

import com.google.gson.Gson
import com.kappdev.moodtracker.domain.util.SettingsTransformer
import java.time.LocalTime

data class Reminder(
    val enabled: Boolean = false,
    val time: LocalTime = LocalTime.of(21, 0)
)

class ReminderTransformer : SettingsTransformer<Reminder, String> {
    override fun serialize(value: Reminder): String {
        return Gson().toJson(value)
    }

    override fun deserialize(parcelable: String): Reminder {
        return Gson().fromJson(parcelable, Reminder::class.java)
    }
}