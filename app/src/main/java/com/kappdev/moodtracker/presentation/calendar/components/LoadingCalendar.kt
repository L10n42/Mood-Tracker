package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.CalendarMonth
import com.kappdev.moodtracker.domain.util.isToday
import com.kappdev.moodtracker.domain.util.sameMonthWith
import com.kappdev.moodtracker.presentation.common.components.AnimatedShimmer
import java.time.LocalDate

@Composable
fun ShimmerCalendar(
    month: CalendarMonth,
    calendarDate: LocalDate,
    modifier: Modifier = Modifier
) {
    AnimatedShimmer(
        colors = listOf(
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onBackground
        ),
        content = { shimmerBrush ->
            LoadingCalendarView(shimmerBrush, month, calendarDate, modifier)
        }
    )
}

@Composable
private fun LoadingCalendarView(
    brush: Brush,
    month: CalendarMonth,
    calendarDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val weekDays = stringArrayResource(R.array.week_days)
    CalendarLayout(
        modifier = modifier,
        itemsPadding = 4.dp,
        streaks = emptyList(),
        weekDayContent = {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        },
        datesContent = {
            month.days.forEach { day ->
                val textColor = when {
                    day.isToday() -> MaterialTheme.colorScheme.primary
                    day.sameMonthWith(calendarDate) -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onBackground
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .aspectRatio(1f)
                                .background(brush, CircleShape)
                        )
                    }

                    Text(
                        text = getCalendarDateStringOf(day, calendarDate),
                        fontSize = 12.sp,
                        color = textColor,
                        fontWeight = if (day.isToday()) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    )
}