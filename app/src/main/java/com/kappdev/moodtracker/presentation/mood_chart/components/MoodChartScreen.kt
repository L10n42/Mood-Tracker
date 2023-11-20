package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.presentation.common.components.MonthSwitchTopBar
import java.time.LocalDate

@Composable
fun MoodChartScreen(
    navController: NavHostController
) {

    Scaffold(
        topBar = {
            MonthSwitchTopBar(date = LocalDate.now(), onDateChange = {})
        }
    ) { padValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
        ) {
            MoodChart(
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}