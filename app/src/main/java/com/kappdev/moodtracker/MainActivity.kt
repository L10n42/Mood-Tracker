package com.kappdev.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.navigation.SetupNavGraph
import com.kappdev.moodtracker.ui.theme.MoodTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settings: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme by settings.getValueAsState(Settings.Theme)

            MoodTrackerTheme(theme = Theme.valueOf(theme)) {
                val navController = rememberNavController()
                SetupNavGraph(navController, settings)
            }
        }
    }
}
