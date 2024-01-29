package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kappdev.moodtracker.domain.util.getMonthName
import com.kappdev.moodtracker.domain.util.isNextMonthAfter
import com.kappdev.moodtracker.domain.util.minusMonth
import com.kappdev.moodtracker.domain.util.nextMonthEnabled
import com.kappdev.moodtracker.domain.util.plusMonth
import com.kappdev.moodtracker.presentation.calendar.CalendarState
import com.kappdev.moodtracker.presentation.calendar.CalendarViewModel
import com.kappdev.moodtracker.presentation.common.components.convexEffect
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarView(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier,
    onDateClick: (date: LocalDate) -> Unit
) {
    val swipeableState = rememberSwipeableState(SwipeState.START)
    val threshold = with(LocalDensity.current) { 24.dp.toPx() }

    LaunchedEffect(swipeableState.currentValue) {
        when {
            swipeableState.currentValue == SwipeState.PREVIOUS -> {
                viewModel.changeCalendarDate(viewModel.calendarDate.minusMonth())
            }
            swipeableState.currentValue == SwipeState.NEXT -> {
                if (viewModel.calendarDate.nextMonthEnabled()) {
                    viewModel.changeCalendarDate(viewModel.calendarDate.plusMonth())
                }
            }
        }

        if (swipeableState.currentValue != SwipeState.START) {
            swipeableState.snapTo(SwipeState.START)
        }
    }

    val calendarModifier = modifier
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
        .convexEffect(RoundedCornerShape(16.dp))
        .padding(16.dp)
        .swipeable(
            state = swipeableState,
            anchors = mapOf(
                0f to SwipeState.START,
                threshold to SwipeState.PREVIOUS,
                -threshold to SwipeState.NEXT
            ),
            orientation = Orientation.Horizontal
        )

    Crossfade(
        targetState = viewModel.calendarState,
        label = "Calendar state change animation"
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

private enum class SwipeState {
    START, PREVIOUS, NEXT
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