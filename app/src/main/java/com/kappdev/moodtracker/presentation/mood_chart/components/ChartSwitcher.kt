package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.presentation.common.components.doubleInnerShadow
import com.kappdev.moodtracker.presentation.mood_chart.ChartFrame
import com.kappdev.moodtracker.presentation.mood_chart.ChartType

@Composable
fun ChartTypeSwitcher(
    selected: ChartType,
    modifier: Modifier = Modifier,
    onSelect: (newType: ChartType) -> Unit
) {
    ChartTypeSwitcherLayout(
        selectedTabPosition = selected.ordinal,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, CircleShape)
            .doubleInnerShadow(
                firstColor = Color.Black.copy(0.5f),
                secondColor = Color.White.copy(0.5f),
                shape = CircleShape,
                offsetY = 1.dp,
                offsetX = 1.dp
            )
    ) {
        ChartType.values().forEach { type ->
            SwitchItem(
                title = stringResource(type.titleRes),
                isSelected = (type == selected),
                onClick = { onSelect(type) }
            )
        }
    }
}

@Composable
fun ChartFrameSwitcher(
    selected: ChartFrame,
    modifier: Modifier = Modifier,
    onSelect: (newType: ChartFrame) -> Unit
) {
    ChartTypeSwitcherLayout(
        selectedTabPosition = selected.ordinal,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, CircleShape)
            .doubleInnerShadow(
                firstColor = Color.Black.copy(0.5f),
                secondColor = Color.White.copy(0.5f),
                shape = CircleShape,
                offsetY = 1.dp,
                offsetX = 1.dp
            )
    ) {
        ChartFrame.values().forEach { type ->
            SwitchItem(
                title = stringResource(type.titleRes),
                isSelected = (type == selected),
                onClick = { onSelect(type) }
            )
        }
    }
}

@Composable
private fun SwitchItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = title,
        modifier = Modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        fontSize = 16.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = when {
            isSelected -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onBackground
        }
    )
}

@Composable
private fun ChartTypeSwitcherLayout(
    selectedTabPosition: Int,
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    indicatorShape: Shape = CircleShape,
    tabItem: @Composable () -> Unit
) {
    SubcomposeLayout(
        modifier = modifier.selectableGroup()
    ) { constraints ->
        val tabMeasurable: List<Placeable> = subcompose(SubComposeId.PRE_CALCULATE_ITEM, tabItem)
            .map { it.measure(constraints) }

        val itemsCount = tabMeasurable.size
        val maxItemWidth = tabMeasurable.maxOf { it.width }
        val maxItemHeight = tabMeasurable.maxOf { it.height }

        val tabPlaceables = subcompose(SubComposeId.ITEM, tabItem).map {
            val fixedSizeConstraints = constraints.copy(
                minWidth = maxItemWidth,
                maxWidth = maxItemWidth,
                minHeight = maxItemHeight,
                maxHeight = maxItemHeight
            )
            it.measure(fixedSizeConstraints)
        }

        val tabRowWidth = maxItemWidth * itemsCount

        val tabPositions = List(itemsCount) { index ->
            val leftX = maxItemWidth * index
            val itemWidth = tabPlaceables[index].width
            TabPosition(leftX.toDp(), itemWidth.toDp())
        }

        layout(tabRowWidth, maxItemHeight) {
            subcompose(SubComposeId.INDICATOR) {
                Box(
                    modifier = Modifier
                        .tabIndicator(tabPositions[selectedTabPosition])
                        .fillMaxWidth()
                        .height(maxItemHeight.toDp())
                        .background(color = indicatorColor, shape = indicatorShape)
                        .doubleInnerShadow(
                            firstColor = Color.White.copy(0.7f),
                            secondColor = Color.Black.copy(0.7f),
                            shape = CircleShape,
                        )
                )
            }.forEach {
                it.measure(Constraints.fixed(tabRowWidth, maxItemHeight)).placeRelative(0, 0)
            }

            tabPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(maxItemWidth * index, 0)
            }
        }
    }
}

private fun Modifier.tabIndicator(
    tabPosition: TabPosition
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "current tab width"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "indicator offset"
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .fillMaxHeight()
}

private data class TabPosition(
    val left: Dp, val width: Dp
)

private enum class SubComposeId {
    PRE_CALCULATE_ITEM,
    ITEM,
    INDICATOR
}