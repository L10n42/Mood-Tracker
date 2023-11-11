package com.kappdev.moodtracker.presentation.navigation

sealed class Screen(val route: String) {
    data object Calendar: Screen("calendar")
    data object MoodChart: Screen("mood_chart")
    data object Options: Screen("options")
    data object Mood: Screen("mood")
}
