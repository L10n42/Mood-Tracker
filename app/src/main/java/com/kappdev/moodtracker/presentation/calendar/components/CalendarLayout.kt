package com.kappdev.moodtracker.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kappdev.moodtracker.domain.model.MoodStreak
import com.kappdev.moodtracker.domain.model.size
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun CalendarLayout(
    modifier: Modifier = Modifier,
    columns: Int = 7,
    streaks: List<MoodStreak>,
    itemsPadding: Dp = 0.dp,
    weekDayContent: @Composable () -> Unit,
    datesContent: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val columnWidth = ((constraints.maxWidth - itemsPadding.toPx() * (columns - 1)) / columns).roundToInt()

        val weekDaysPlaceables = subcompose("week-day-names", weekDayContent).map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = columnWidth,
                    maxWidth = columnWidth,
                )
            )
        }

        val datePlaceables = subcompose("date-items", datesContent).map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = columnWidth,
                    maxWidth = columnWidth
                )
            )
        }

        val weekDaysMaxHeight = weekDaysPlaceables.maxOf { it.height }

        val datesContentHeight = datePlaceables.chunked(7) { placeable ->
            placeable.maxOf { it.height }
        }.sum()

        val rowCount = ceil(datePlaceables.size.toDouble() / columns.toDouble()).toInt()
        val totalSpacesHeight = itemsPadding.roundToPx() * rowCount
        val totalViewHeight = datesContentHeight + totalSpacesHeight + weekDaysMaxHeight

        layout(constraints.maxWidth, totalViewHeight) {

            streaks.forEach { streak ->
                subcompose("streak-from-${streak.range.first}-to-${streak.range.second}") {
                    StreakView(streak)
                }.forEach { measurable ->
                    val strickWidth = (streak.size * columnWidth) + itemsPadding.toPx() * (streak.size - 1)

                    val currentRow = floor(streak.range.first.toDouble() / columns.toDouble()).toInt()
                    val positionInRow = streak.range.first - (columns * currentRow)

                    var strickY = weekDaysMaxHeight + itemsPadding.roundToPx()
                    val strickX = (columnWidth * positionInRow) + (itemsPadding.roundToPx() * positionInRow)

                    (0 until currentRow).forEach { row ->
                        val currentRowMaxHeight = datePlaceables.chunked(7).get(row).maxOf { it.height }
                        strickY += currentRowMaxHeight
                        strickY += itemsPadding.roundToPx()
                    }

                    measurable.measure(Constraints.fixed(strickWidth.roundToInt(), columnWidth))
                        .placeRelative(strickX, strickY)
                }
            }

            var weekDayX = 0

            weekDaysPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(weekDayX, 0)

                weekDayX += placeable.width
                if (index != columns - 1) {
                    weekDayX += itemsPadding.roundToPx()
                }
            }

            var dateX = 0
            var dateY = weekDaysMaxHeight + itemsPadding.roundToPx()
            var currentColumn = 1
            var currentRow = 0

            datePlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(dateX, dateY)

                dateX += placeable.width
                if (currentColumn != columns) {
                    dateX += itemsPadding.roundToPx()
                }

                if (currentColumn == columns && index != datePlaceables.lastIndex) {
                    val currentRowMaxHeight = datePlaceables.chunked(7).get(currentRow).maxOf { it.height }
                    dateX = 0
                    dateY += currentRowMaxHeight
                    dateY += itemsPadding.roundToPx()
                    currentColumn = 1
                    currentRow++
                } else {
                    currentColumn++
                }
            }
        }
    }
}

@Composable
private fun StreakView(
    streak: MoodStreak
) {
    val startPercent = if (streak.isOpen) 0 else 50
    val endPercent = if (streak.isClose) 0 else 50

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = streak.moodType.color.copy(0.64f),
                shape = RoundedCornerShape(
                    topStartPercent = startPercent,
                    bottomStartPercent = startPercent,
                    topEndPercent = endPercent,
                    bottomEndPercent = endPercent
                )
            )
    )
}