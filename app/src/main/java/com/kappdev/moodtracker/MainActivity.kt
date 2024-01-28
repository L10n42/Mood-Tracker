package com.kappdev.moodtracker

import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kappdev.moodtracker.data.receiver.ReminderReceiver
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.MainScreen
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.navigation.NavConst
import com.kappdev.moodtracker.presentation.navigation.Screen
import com.kappdev.moodtracker.presentation.navigation.SetupNavGraph
import com.kappdev.moodtracker.presentation.navigation.navigateWithValue
import com.kappdev.moodtracker.ui.theme.MoodTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settings: SettingsManager

    @Inject
    lateinit var notificationManager: NotificationManager

    private var theme by mutableStateOf<Theme?>(null)
    private var mainScreen by mutableStateOf<MainScreen?>(null)
    private var isReminderIntent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isReminderIntent = intent.getBooleanExtra(ReminderReceiver.IS_REMINDER_INTENT, false)
        if (isReminderIntent) {
            notificationManager.cancel(ReminderReceiver.NOTIFICATION_ID)
        }

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

                    SetupNavGraph(navController, validMainScreen.route, settings, isReminderIntent)
                }
            }
        }
    }

    private fun launchThemeListener() {
        lifecycleScope.launch {
            settings.getValueFlow(Settings.Theme).collectLatest { theme ->
                this@MainActivity.theme = theme
            }
        }
    }

    private fun launchMainScreenListener() {
        lifecycleScope.launch {
            this@MainActivity.mainScreen = settings.getValueFlow(Settings.MainScreen).first()
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
