package com.kappdev.moodtracker.domain.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.kappdev.moodtracker.domain.util.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsManager {

    @Composable
    fun <T> getValueAsState(settings: Settings<T>): State<T>

    suspend fun <T> getValueFlow(settings: Settings<T>): Flow<T>

    suspend fun <T> setValueTo(settings: Settings<T>, value: T)

}