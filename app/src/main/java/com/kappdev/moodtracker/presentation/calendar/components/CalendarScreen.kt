package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Quote
import com.kappdev.moodtracker.domain.model.QuoteBlock
import com.kappdev.moodtracker.presentation.calendar.CalendarViewModel
import com.kappdev.moodtracker.presentation.common.components.ActionButton
import com.kappdev.moodtracker.presentation.common.components.DividedContent
import com.kappdev.moodtracker.presentation.common.components.MonthSwitchTopBar
import com.kappdev.moodtracker.presentation.common.components.QuoteBlock
import com.kappdev.moodtracker.presentation.navigation.NavConst
import com.kappdev.moodtracker.presentation.navigation.Screen
import com.kappdev.moodtracker.presentation.navigation.navigateWithValue
import java.time.LocalDate

@Composable
fun CalendarScreen(
    navController: NavHostController,
    quoteBlock: QuoteBlock?,
    dailyQuote: Quote?,
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

            CalendarView(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onDateClick = { date ->
                    viewModel.ifDateIsValid(date) {
                        navController.navigateWithValue(
                            route = Screen.Mood.route,
                            valueKey = NavConst.DATE_KEY,
                            value = date
                        )
                    }
                }
            )

            if (dailyQuote != null && quoteBlock?.onCalendarScreen == true) {
                QuoteBlock(
                    quote = dailyQuote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        viewModel.copyQuote(dailyQuote)
                    }
                )
            }

            val actionButtonModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

            ActionButton(
                title = stringResource(R.string.today_mood_question),
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
                onClick = {
                    navController.navigate(Screen.MoodChart.route)
                }
            )

            ActionButton(
                title = stringResource(R.string.btn_options),
                icon = Icons.Rounded.Settings,
                modifier = actionButtonModifier,
                onClick = {
                    navController.navigate(Screen.Options.route)
                }
            )
        }
    }
}