package com.kappdev.moodtracker.data.repository

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_datastore")

class SettingsManagerImpl @Inject constructor(
    private val context: Context
) : SettingsManager {

    @Composable
    override fun <T> getValueAsState(settings: Settings<T>): State<T> {
        return getValueBy(settings).collectAsState(settings.default)
    }

    private fun <T> getValueBy(setting: Settings<T>) = context.dataStore.data.map { preferences ->
        preferences[setting.key] ?: setting.default
    }

    override suspend fun <T> setValueTo(settings: Settings<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[settings.key] = value
        }
    }
}