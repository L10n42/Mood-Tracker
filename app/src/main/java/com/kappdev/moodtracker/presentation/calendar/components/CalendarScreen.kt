package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.calendar.CalendarViewModel
import com.kappdev.moodtracker.presentation.common.components.ActionButton
import com.kappdev.moodtracker.presentation.common.components.DividedContent
import com.kappdev.moodtracker.presentation.navigation.NavConst
import com.kappdev.moodtracker.presentation.navigation.Screen
import com.kappdev.moodtracker.presentation.navigation.navigateWithValue
import java.time.LocalDate

@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.launch()
    }

    Scaffold(
        topBar = {
            DividedContent(
                isDividerVisible = scrollState.canScrollBackward
            ) {
                MonthSwitchTopBar(
                    date = viewModel.calendarDate,
                    onDateChange = viewModel::changeCalendarDate
                )
            }
        }
    ) { padValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MoodCalendar(
                weekDays = stringArrayResource(R.array.week_days),
                calendarData = viewModel.data,
                calendarDate = viewModel.calendarDate,
                streaks = viewModel.moodStreaks,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                onDateClick = { date ->
                    navController.navigateWithValue(
                        route = Screen.Mood.route,
                        valueKey = NavConst.DATE_KEY,
                        value = date
                    )
                }
            )

            val actionButtonModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

            ActionButton(
                title = stringResource(R.string.btn_today_mood),
                modifier = actionButtonModifier,
                onClick = {
                    navController.navigateWithValue(
                        route = Screen.Mood.route,
                        valueKey = NavConst.DATE_KEY,
                        value = LocalDate.now()
                    )
                }
            )

            ActionButton(
                title = stringResource(R.string.btn_mood_chart),
                icon = Icons.Rounded.BarChart,
                modifier = actionButtonModifier,
                onClick = { /* TODO */ }
            )

            ActionButton(
                title = stringResource(R.string.btn_options),
                icon = Icons.Rounded.Settings,
                modifier = actionButtonModifier,
                onClick = { /* TODO */ }
            )
        }
    }
}