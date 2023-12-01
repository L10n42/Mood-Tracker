package com.kappdev.moodtracker.domain.util

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kappdev.moodtracker.domain.model.ReminderTransformer
import com.kappdev.moodtracker.domain.model.Reminder as MoodReminder
import com.kappdev.moodtracker.domain.util.Theme as AppTheme
import com.kappdev.moodtracker.domain.util.MainScreen as AppMainScreen

sealed class Settings<V, P>(val key: Preferences.Key<P>, val default: V, val transformer: SettingsTransformer<V, P>) {
    data object Theme : Settings<AppTheme, String>(
        stringPreferencesKey("THEME"),
        AppTheme.SYSTEM_DEFAULT,
        ThemeTransformer()
    )

    data object MainScreen : Settings<AppMainScreen, String>(
        stringPreferencesKey("MAIN_SCREEN"),
        AppMainScreen.Calendar,
        MainScreenTransformer()
    )

    data object Reminder : Settings<MoodReminder, String>(
        stringPreferencesKey("REMAINDER"),
        MoodReminder(),
        ReminderTransformer()
    )
}

interface SettingsTransformer<V, P> {
    fun serialize(value: V): P
    fun deserialize(parcelable: P): V
}