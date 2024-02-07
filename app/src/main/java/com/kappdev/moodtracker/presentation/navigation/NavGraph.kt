package com.kappdev.moodtracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kappdev.moodtracker.domain.model.Quote
import com.kappdev.moodtracker.domain.model.QuoteBlock
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.presentation.calendar.components.CalendarScreen
import com.kappdev.moodtracker.presentation.mood_chart.components.MoodChartScreen
import com.kappdev.moodtracker.presentation.mood_screen.components.MoodScreen
import com.kappdev.moodtracker.presentation.options.components.OptionsScreen
import java.time.LocalDate

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    settings: SettingsManager,
    isReminderIntent: Boolean,
    dailyQuote: Quote?,
    quoteBlock: QuoteBlock?
) {
    NavHost(
        navController = navController,
        startDestination = if (isReminderIntent) Screen.Mood.route else startDestination
    ) {
        composable(
            Screen.Calendar.route,
            enterTransition = {
                when (getInitialRoute()) {
                    Screen.Mood.route -> slideInRight()
                    else -> slideInLeft()
                }
            },
            exitTransition = { slideOutLeft() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) {
            CalendarScreen(navController, quoteBlock, dailyQuote)
        }

        composable(
            Screen.Mood.route,
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) { stackEntry ->
            val date = stackEntry.catchValue<LocalDate>(NavConst.DATE_KEY) ?: LocalDate.now()
            MoodScreen(date) {
                if (!navController.popBackStack()) {
                    navController.navigate(startDestination)
                }
            }
        }

        composable(
            Screen.MoodChart.route,
            enterTransition = {
                when (getInitialRoute()) {
                    Screen.Mood.route -> slideInRight()
                    else -> slideInLeft()
                }
            },
            exitTransition = { slideOutLeft() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) {
            MoodChartScreen(navController, quoteBlock, dailyQuote)
        }

        composable(
            Screen.Options.route,
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
            popEnterTransition = { slideInLeft() },
            popExitTransition = { slideOutRight() }
        ) {
            OptionsScreen(navController, settings)
        }
    }
}