package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodStreak
import com.kappdev.moodtracker.domain.util.getMonthName
import com.kappdev.moodtracker.domain.util.isToday
import com.kappdev.moodtracker.domain.util.sameMonthWith
import java.time.LocalDate

@Composable
fun MoodCalendar(
    weekDays: Array<String>,
    calendarData: Map<LocalDate, Mood?>,
    streaks: List<MoodStreak>,
    calendarDate: LocalDate,
    modifier: Modifier = Modifier,
    onDateClick: (date: LocalDate) -> Unit
) {
    CalendarLayout(
        modifier = modifier,
        itemsPadding = 4.dp,
        streaks = streaks,
        weekDayContent = {
            weekDays.forEach { day ->
                WeekDayView(day)
            }
        },
        datesContent = {
            calendarData.forEach { entry ->
                DateView(entry, calendarDate) {
                    onDateClick(entry.key)
                }
            }
        }
    )
}

@Composable
private fun DateView(
    entry: Map.Entry<LocalDate, Mood?>,
    calendarDate: LocalDate,
    onClick: () -> Unit
) {
    val textColor = when {
        entry.key.isToday() -> MaterialTheme.colorScheme.primary
        entry.key.sameMonthWith(calendarDate) -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onBackground
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val mood = entry.value
        if (mood != null) {
            LottieAnimationView(mood = mood, onClick = onClick)
        } else {
            EmptyDateView(onClick = onClick)
        }

        Text(
            text = getDateStringOf(entry.key, calendarDate),
            fontSize = 12.sp,
            color = textColor,
            fontWeight = if (entry.key.isToday()) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun LottieAnimationView(
    mood: Mood,
    onClick: () -> Unit
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(mood.type.animationRes))

    LottieAnimation(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        contentScale = ContentScale.Crop,
        isPlaying = true,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun EmptyDateView(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .aspectRatio(1f)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
    )
}

private fun getDateStringOf(date: LocalDate, calendarDate: LocalDate): String {
    val isNextMonth = date.isNextMonthAfter(calendarDate)
    val isFirstDay = (date.dayOfMonth == 1)
    return if (isNextMonth && isFirstDay) {
        date.getMonthName(short = true)
    } else {
        date.dayOfMonth.toString()
    }
}

private fun LocalDate.isNextMonthAfter(date: LocalDate): Boolean {
    return (this.year == date.year && this.month == date.month + 1)
}

@Composable
private fun WeekDayView(
    name: String
) {
    Text(
        text = name,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        fontSize = 14.sp
    )
}