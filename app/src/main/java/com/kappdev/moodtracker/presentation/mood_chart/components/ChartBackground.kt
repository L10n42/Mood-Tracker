package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.chartBackground(
    color: Color,
    labelHeight: Dp = 0.dp,
) = this.drawBehind {
    val gapHeight = ((this.size.height - labelHeight.toPx()) / 5)

    (0..4).forEach { line ->
        drawLine(
            color = color,
            start = Offset(0f, line * gapHeight),
            end = Offset(size.width, line * gapHeight),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                phase = 0f
            )
        )
    }

    drawLine(
        color = color,
        start = Offset(0f, this.size.height - labelHeight.toPx()),
        end = Offset(size.width, this.size.height - labelHeight.toPx()),
        strokeWidth = 1.dp.toPx(),
        cap = StrokeCap.Round
    )
}