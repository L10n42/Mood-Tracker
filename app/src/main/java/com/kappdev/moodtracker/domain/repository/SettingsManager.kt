package com.kappdev.moodtracker.domain.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.datastore.preferences.core.Preferences
import com.kappdev.moodtracker.domain.util.Settings

interface SettingsManager {

    @Composable
    fun <T> getValueAsState(settings: Settings<T>): State<T>

    suspend fun <T> setValueTo(settings: Settings<T>, value: T)

}