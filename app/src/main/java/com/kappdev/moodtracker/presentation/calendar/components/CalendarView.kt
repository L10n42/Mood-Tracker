package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kappdev.moodtracker.domain.util.getMonthName
import com.kappdev.moodtracker.domain.util.isNextMonthAfter
import com.kappdev.moodtracker.presentation.calendar.CalendarState
import com.kappdev.moodtracker.presentation.calendar.CalendarViewModel
import com.kappdev.moodtracker.presentation.common.components.convexEffect
import java.time.LocalDate

@Composable
fun CalendarView(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier,
    onDateClick: (date: LocalDate) -> Unit
) {
    val calendarModifier = modifier
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
        .convexEffect(RoundedCornerShape(16.dp))
        .padding(16.dp)

    Crossfade(
        targetState = viewModel.calendarState,
        label = "calendar state change animation"
    ) { state ->
        when (state) {
            CalendarState.IDLE, CalendarState.LOADING -> {
                ShimmerCalendar(
                    month = viewModel.calendarMonth,
                    calendarDate = viewModel.calendarDate,
                    modifier = calendarModifier
                )
            }
            CalendarState.READY -> {
                MoodCalendar(
                    calendarData = viewModel.data,
                    calendarDate = viewModel.calendarDate,
                    streaks = viewModel.moodStreaks,
                    modifier = calendarModifier,
                    onDateClick = onDateClick
                )
            }
        }
    }
}

fun getCalendarDateStringOf(date: LocalDate, calendarDate: LocalDate): String {
    val isNextMonth = date.isNextMonthAfter(calendarDate)
    val isFirstDay = (date.dayOfMonth == 1)
    return if (isNextMonth && isFirstDay) {
        date.getMonthName(short = true)
    } else {
        date.dayOfMonth.toString()
    }
}