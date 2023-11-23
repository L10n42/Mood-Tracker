package com.kappdev.moodtracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.presentation.calendar.components.CalendarScreen
import com.kappdev.moodtracker.presentation.mood_chart.components.MoodChartScreen
import com.kappdev.moodtracker.presentation.mood_screen.components.MoodScreen
import com.kappdev.moodtracker.presentation.options.components.OptionsScreen
import java.time.LocalDate

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    settings: SettingsManager
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route
    ) {
        composable(Screen.Calendar.route) {
            CalendarScreen(navController)
        }

        composable(Screen.Mood.route) { stackEntry ->
            val date = stackEntry.catchValue<LocalDate>(NavConst.DATE_KEY)
            MoodScreen(navController, date)
        }

        composable(Screen.MoodChart.route) {
            MoodChartScreen(navController)
        }

        composable(Screen.Options.route) {
            OptionsScreen(navController, settings)
        }
    }
}