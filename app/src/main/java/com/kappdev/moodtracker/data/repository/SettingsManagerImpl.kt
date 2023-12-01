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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_datastore")

class SettingsManagerImpl @Inject constructor(
    private val context: Context
) : SettingsManager {

    @Composable
    override fun <V, P> getValueAsState(settings: Settings<V, P>): State<V> {
        return getValueBy(settings).collectAsState(settings.default)
    }

    override suspend fun <V, P> getValueFlow(settings: Settings<V, P>): Flow<V> {
        return getValueBy(settings)
    }

    private fun <V, P> getValueBy(settings: Settings <V, P>) = context.dataStore.data.map { preferences ->
        val value = preferences[settings.key] ?: settings.transformer.serialize(settings.default)
        settings.transformer.deserialize(value)
    }

    override suspend fun <V, P> setValueTo(settings: Settings<V, P>, value: V) {
        context.dataStore.edit { preferences ->
            preferences[settings.key] = settings.transformer.serialize(value)
        }
    }
}