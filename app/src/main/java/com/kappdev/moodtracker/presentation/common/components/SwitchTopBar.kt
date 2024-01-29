package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.domain.util.getMonthName
import com.kappdev.moodtracker.domain.util.isCurrentYear
import com.kappdev.moodtracker.domain.util.minusMonth
import com.kappdev.moodtracker.domain.util.minusWeek
import com.kappdev.moodtracker.domain.util.nextMonthEnabled
import com.kappdev.moodtracker.domain.util.plusMonth
import com.kappdev.moodtracker.domain.util.plusWeek
import com.kappdev.moodtracker.domain.util.sameMonthWith
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun WeekSwitchTopBar(
    date: LocalDate,
    onDateChange: (newDate: LocalDate) -> Unit
) {
    BasicSwitchTopBar(
        date = date,
        isNextEnabled = date.nextWeekEnabled(),
        titleTransform = {
            val weekStart = it.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weekEnd = it.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            getWeekTitle(weekStart, weekEnd)
        },
        onBack = {
            onDateChange(date.minusWeek())
        },
        onNext = {
            onDateChange(date.plusWeek())
        }
    )
}

private fun getWeekTitle(start: LocalDate, end: LocalDate): String {
    return if (start.sameMonthWith(end)) {
        "${start.getMonthName()} ${start.dayOfMonth} - ${end.dayOfMonth}"
    } else {
        "${styleWeekDate(start)} - ${styleWeekDate(end)}"
    }
}

private fun styleWeekDate(date: LocalDate) = buildString {
    append(date.getMonthName(!date.isCurrentYear()))
    append(" ")
    append(date.dayOfMonth)
    if (!date.isCurrentYear()) {
        append(", ")
        append(date.year)
    }
}

private fun LocalDate.nextWeekEnabled(): Boolean {
    val currentDate = LocalDate.now()
    val currentWeekYear = currentDate.get(WeekFields.of(Locale.getDefault()).weekBasedYear())
    val currentWeek = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

    val dateWeekYear = this.get(WeekFields.of(Locale.getDefault()).weekBasedYear())
    val dateWeek = this.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

    return if (dateWeekYear == currentWeekYear) {
        dateWeek < currentWeek
    } else {
        dateWeekYear < currentWeekYear
    }
}

@Composable
fun MonthSwitchTopBar(
    date: LocalDate,
    onDateChange: (newDate: LocalDate) -> Unit
) {
    BasicSwitchTopBar(
        date = date,
        isNextEnabled = date.nextMonthEnabled(),
        titleTransform = {
            "${it.getMonthName()} ${it.year}"
        },
        onBack = {
            onDateChange(date.minusMonth())
        },
        onNext = {
            onDateChange(date.plusMonth())
        }
    )
}

@Composable
private fun BasicSwitchTopBar(
    date: LocalDate,
    titleTransform: (date: LocalDate) -> String,
    isNextEnabled: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp)
    ) {
        SwitchButton(
            icon = Icons.Rounded.ArrowBackIos,
            onClick = onBack
        )

        AnimatedDateTitle(
            date = date,
            titleTransform = titleTransform,
            modifier = Modifier.weight(1f)
        )

        SwitchButton(
            icon = Icons.Rounded.ArrowForwardIos,
            enabled = isNextEnabled,
            onClick = onNext
        )
    }
}

@Composable
private fun SwitchButton(
    icon: ImageVector,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: () -> Unit
) {
    val color = when {
        enabled -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onBackground
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .border(width = 2.dp, color = color, shape = shape)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = null
        )
    }
}

@Composable
private fun AnimatedDateTitle(
    date: LocalDate,
    titleTransform: (date: LocalDate) -> String,
    modifier: Modifier = Modifier,
    duration: Int = 700
) {
    var oldDate by remember { mutableStateOf(date) }

    SideEffect {
        oldDate = date
    }

    AnimatedContent(
        targetState = date,
        modifier = modifier,
        transitionSpec = {
            val slideDirection = when {
                date > oldDate -> AnimatedContentTransitionScope.SlideDirection.Left
                else -> AnimatedContentTransitionScope.SlideDirection.Right
            }
            slideIntoContainer(
                towards = slideDirection, animationSpec = tween(duration)
            ) + fadeIn(
                animationSpec = tween(duration)
            ) togetherWith
                slideOutOfContainer(
                    towards = slideDirection, animationSpec = tween(duration)
                ) + fadeOut(
                    animationSpec = tween(duration)
                )
        },
        label = "Animated date title"
    ) {
        DateTitle(titleTransform(it))
    }
}

@Composable
private fun DateTitle(
    title: String
) {
    Text(
        text = title,
        maxLines = 1,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface
    )
}