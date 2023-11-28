package com.kappdev.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.MainScreen
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.navigation.SetupNavGraph
import com.kappdev.moodtracker.ui.theme.MoodTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settings: SettingsManager

    private var theme by mutableStateOf<Theme?>(null)
    private var mainScreen by mutableStateOf<MainScreen?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchThemeListener()
        launchMainScreenListener()

        setContent {
            ifDataValid { validTheme, validMainScreen ->
                MoodTrackerTheme(theme = validTheme) {
                    val navController = rememberNavController()
                    val systemUiController = rememberSystemUiController()
                    val backgroundColor = MaterialTheme.colorScheme.background

                    SideEffect {
                        systemUiController.setSystemBarsColor(backgroundColor)
                    }

                    SetupNavGraph(navController, validMainScreen.route, settings)
                }
            }
        }
    }

    private fun launchThemeListener() {
        lifecycleScope.launch {
            settings.getValueFlow(Settings.Theme).collectLatest { themeName ->
                theme = Theme.valueOf(themeName)
            }
        }
    }

    private fun launchMainScreenListener() {
        lifecycleScope.launch {
            val mainScreenName = settings.getValueFlow(Settings.MainScreen).first()
            mainScreen = MainScreen.valueOf(mainScreenName)
        }
    }

    private inline fun ifDataValid(block: (Theme, MainScreen) -> Unit) {
        theme?.let { validTheme ->
            mainScreen?.let { validMainScreen ->
                block(validTheme, validMainScreen)
            }
        }
    }
}
