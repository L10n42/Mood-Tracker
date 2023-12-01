package com.kappdev.moodtracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.presentation.calendar.components.CalendarScreen
import com.kappdev.moodtracker.presentation.mood_chart.components.MoodChartScreen
import com.kappdev.moodtracker.presentation.mood_screen.components.MoodScreen
import com.kappdev.moodtracker.presentation.options.components.OptionsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    settings: SettingsManager,
    isReminderIntent: Boolean
) {

    LaunchedEffect(Unit) {
        val firstBackStackEntry = navController.currentBackStackEntryFlow.first()
        if (firstBackStackEntry.destination.route == startDestination && isReminderIntent) {
            navController.navigateWithValue(
                route = Screen.Mood.route,
                valueKey = NavConst.DATE_KEY,
                value = LocalDate.now()
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            Screen.Calendar.route,
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutLeft() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) {
            CalendarScreen(navController)
        }

        composable(
            Screen.Mood.route,
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) { stackEntry ->
            val date = stackEntry.catchValue<LocalDate>(NavConst.DATE_KEY)
            MoodScreen(navController, date)
        }

        composable(
            Screen.MoodChart.route,
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutLeft() },
            popEnterTransition = { slideInRight() },
            popExitTransition = { slideOutRight() }
        ) {
            MoodChartScreen(navController)
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