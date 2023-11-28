package com.kappdev.moodtracker.domain.util

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kappdev.moodtracker.domain.util.Theme as AppTheme
import com.kappdev.moodtracker.domain.util.MainScreen as AppMainScreen

sealed class Settings<T>(val key: Preferences.Key<T>, val default: T) {
    data object Theme: Settings<String>(stringPreferencesKey("THEME"), AppTheme.SYSTEM_DEFAULT.name)
    data object MainScreen: Settings<String>(stringPreferencesKey("MAIN_SCREEN"), AppMainScreen.Calendar.name)
}