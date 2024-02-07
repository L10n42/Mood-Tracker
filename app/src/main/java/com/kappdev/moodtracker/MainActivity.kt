package com.kappdev.moodtracker

import android.app.NotificationManager
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
import com.kappdev.moodtracker.data.receiver.ReminderReceiver
import com.kappdev.moodtracker.domain.model.Quote
import com.kappdev.moodtracker.domain.model.QuoteBlock
import com.kappdev.moodtracker.domain.repository.QuoteManager
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

    @Inject
    lateinit var quoteManager: QuoteManager

    @Inject
    lateinit var notificationManager: NotificationManager

    private var theme by mutableStateOf<Theme?>(null)
    private var quoteBlock by mutableStateOf<QuoteBlock?>(null)
    private var mainScreen by mutableStateOf<MainScreen?>(null)
    private var dailyQuote by mutableStateOf<Quote?>(null)
    private var isReminderIntent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isReminderIntent = intent.getBooleanExtra(ReminderReceiver.IS_REMINDER_INTENT, false)
        if (isReminderIntent) {
            notificationManager.cancel(ReminderReceiver.NOTIFICATION_ID)
        }

        updateQuoteIfNeed()
        launchQuote()
        launchThemeListener()
        launchMainScreenListener()
        launchQuoteBlockListener()

        setContent {
            ifDataValid { validTheme, validMainScreen ->
                MoodTrackerTheme(theme = validTheme) {
                    val navController = rememberNavController()
                    val systemUiController = rememberSystemUiController()
                    val backgroundColor = MaterialTheme.colorScheme.background

                    SideEffect {
                        systemUiController.setSystemBarsColor(backgroundColor)
                    }

                    SetupNavGraph(
                        navController = navController,
                        startDestination = validMainScreen.route,
                        settings = settings,
                        isReminderIntent = isReminderIntent,
                        dailyQuote = dailyQuote,
                        quoteBlock = quoteBlock
                    )
                }
            }
        }
    }

    private fun updateQuoteIfNeed() {
        lifecycleScope.launch {
            if (quoteManager.needUpdate()) {
                quoteManager.updateQuote()
            }
        }
    }

    private fun launchQuote() {
        lifecycleScope.launch {
            quoteManager.getQuoteFlow().collectLatest { dailyQuote = it }
        }
    }

    private fun launchQuoteBlockListener() {
        lifecycleScope.launch {
            settings.getValueFlow(Settings.QuoteBlock).collectLatest { quoteBlock ->
                this@MainActivity.quoteBlock = quoteBlock
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
