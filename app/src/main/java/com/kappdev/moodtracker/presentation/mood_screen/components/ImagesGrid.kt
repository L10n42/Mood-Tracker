package com.kappdev.moodtracker.presentation.mood_screen.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun ImagesGrid(
    minWidth: Dp,
    modifier: Modifier = Modifier,
    itemsPadding: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val precalculatedItems = subcompose("precalculated-content", content).map {
            it.measure(constraints)
        }

        var maxRowItems = 0
        var accruedRowWidth = 0f
        while (accruedRowWidth < constraints.maxWidth) {
            accruedRowWidth += minWidth.toPx() + itemsPadding.toPx()
            maxRowItems++
        }

        val adaptiveWidth = if (precalculatedItems.size < maxRowItems) {
            val totalHorizontalPadding = (precalculatedItems.size - 1) * itemsPadding.toPx()
            (constraints.maxWidth - totalHorizontalPadding) / precalculatedItems.size
        } else {
            val totalHorizontalPadding = (maxRowItems - 1) * itemsPadding.toPx()
            (constraints.maxWidth - totalHorizontalPadding) / maxRowItems
        }

        val itemPlaceables = subcompose("content", content).map {
            val fixedSizeConstraints = constraints.copy(
                minWidth = adaptiveWidth.toInt(),
                maxWidth = adaptiveWidth.toInt()
            )
            it.measure(fixedSizeConstraints)
        }

        val rowHeights = itemPlaceables.chunked(maxRowItems) { placeable ->
            placeable.maxOf { it.height }
        }

        val rowCount = ceil(itemPlaceables.size.toDouble() / maxRowItems.toDouble()).toInt()
        val totalSpacesHeight = itemsPadding.roundToPx() * rowCount
        val totalViewHeight = rowHeights.sum() + totalSpacesHeight

        layout(constraints.maxWidth, totalViewHeight) {
            var itemX = 0
            var itemY = 0
            var currentColumn = 1
            var currentRow = 0

            itemPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(itemX, itemY)

                itemX += placeable.width
                itemX += itemsPadding.roundToPx()

                if (currentColumn == maxRowItems && index != itemPlaceables.lastIndex) {
                    itemX = 0
                    itemY += rowHeights[currentRow]
                    itemY += itemsPadding.roundToPx()
                    currentColumn = 1
                    currentRow++
                } else {
                    currentColumn++
                }
            }
        }
    }
}