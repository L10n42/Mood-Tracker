package com.kappdev.moodtracker.domain.util

import androidx.annotation.StringRes
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.navigation.Screen

enum class MainScreen(@StringRes val titleRes: Int, val route: String) {
    Calendar(R.string.screen_mood_calendar, Screen.Calendar.route),
    Chart(R.string.screen_mood_chart, Screen.MoodChart.route)
}

class MainScreenTransformer : SettingsTransformer<MainScreen, String> {
    override fun serialize(value: MainScreen): String {
        return value.name
    }

    override fun deserialize(parcelable: String): MainScreen {
        return MainScreen.valueOf(parcelable)
    }
}