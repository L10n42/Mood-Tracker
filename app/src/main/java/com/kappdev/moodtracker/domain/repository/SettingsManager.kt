package com.kappdev.moodtracker.domain.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.util.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsManager {

    @Composable
    fun <V, P> getValueAsState(settings: Settings<V, P>): State<V>

    suspend fun <V, P> getValueFlow(settings: Settings<V, P>): Flow<V>

    suspend fun <V, P> setValueTo(settings: Settings<V, P>, value: V)

}