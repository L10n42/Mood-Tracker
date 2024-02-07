package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
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
import com.kappdev.moodtracker.presentation.common.components.ActionButton
import com.kappdev.moodtracker.presentation.common.components.DividedContent
import com.kappdev.moodtracker.presentation.common.components.MonthSwitchTopBar
import com.kappdev.moodtracker.presentation.common.components.QuoteBlock
import com.kappdev.moodtracker.presentation.common.components.VerticalSpace
import com.kappdev.moodtracker.presentation.common.components.WeekSwitchTopBar
import com.kappdev.moodtracker.presentation.mood_chart.ChartType
import com.kappdev.moodtracker.presentation.mood_chart.MoodChartScreenViewModel
import com.kappdev.moodtracker.presentation.navigation.NavConst
import com.kappdev.moodtracker.presentation.navigation.Screen
import com.kappdev.moodtracker.presentation.navigation.navigateWithValue
import java.time.LocalDate

@Composable
fun MoodChartScreen(
    navController: NavHostController,
    quoteBlock: QuoteBlock?,
    dailyQuote: Quote?,
    viewModel: MoodChartScreenViewModel = hiltViewModel()
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
                when (viewModel.chartType) {
                    ChartType.MONTH -> MonthSwitchTopBar(
                        date = viewModel.currentDate,
                        onDateChange = viewModel::changeCurrentDate
                    )
                    ChartType.WEEK -> WeekSwitchTopBar(
                        date = viewModel.currentDate,
                        onDateChange = viewModel::changeCurrentDate
                    )
                }
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
            MoodChart(
                data = viewModel.data,
                chartType = viewModel.chartType,
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            ChartTypeSwitcher(
                selected = viewModel.chartType,
                onSelect = viewModel::changeChartType
            )

            if (dailyQuote != null && quoteBlock?.onChartScreen == true) {
                QuoteBlock(
                    quote = dailyQuote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp),
                    onClick = {
                        viewModel.copyQuote(dailyQuote)
                    }
                )
            } else {
                VerticalSpace(32.dp)
            }

            ActionButton(
                title = stringResource(R.string.today_mood_question),
                modifier = ActionButtonModifier,
                onClick = {
                    navController.navigateWithValue(
                        route = Screen.Mood.route,
                        valueKey = NavConst.DATE_KEY,
                        value = LocalDate.now()
                    )
                }
            )

            ActionButton(
                title = stringResource(R.string.btn_mood_calendar),
                icon = Icons.Rounded.CalendarMonth,
                modifier = ActionButtonModifier,
                onClick = {
                    navController.navigate(Screen.Calendar.route)
                }
            )

            ActionButton(
                title = stringResource(R.string.btn_options),
                icon = Icons.Rounded.Settings,
                modifier = ActionButtonModifier,
                onClick = {
                    navController.navigate(Screen.Options.route)
                }
            )
        }
    }
}

private val ActionButtonModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)