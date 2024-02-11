package com.kappdev.moodtracker.presentation.mood_chart.components

import android.graphics.PointF
import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.model.values
import com.kappdev.moodtracker.presentation.common.components.convexEffect
import com.kappdev.moodtracker.presentation.mood_chart.ChartFrame

@Composable
fun LineChart(
    data: Map<Int, MoodType?>,
    chartFrame: ChartFrame,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .convexEffect(RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        val entriesCount = data.count { it.value != null }
        if (entriesCount > 1) {
            val (moodAxis, daysAxis, chart, background, divider) = createRefs()

            ChartBackground(
                Modifier.constrainAs(background) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(daysAxis.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )

            MoodAxis(
                Modifier.constrainAs(moodAxis) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(daysAxis.top)
                    height = Dimension.fillToConstraints
                }
            )

            DayAxis(
                days = data.keys,
                chartFrame = chartFrame,
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .constrainAs(daysAxis) {
                        start.linkTo(moodAxis.end)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            AdoptedChartLine(
                data = data,
                chartFrame = chartFrame,
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .constrainAs(chart) {
                        start.linkTo(moodAxis.end)
                        bottom.linkTo(daysAxis.top)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }

            )

            MoodAxisDivider(
                isVisible = scrollState.canScrollBackward,
                Modifier.constrainAs(divider) {
                    start.linkTo(moodAxis.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                    height = Dimension.fillToConstraints
                }
            )
        } else {
            val invalidChart = createRef()
            InvalidChart(
                Modifier.constrainAs(invalidChart) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            )
        }
    }
}

@Composable
private fun InvalidChart(
    modifier: Modifier = Modifier
) {
    val text = stringResource(R.string.msg_invalid_chart)
    val message = buildAnnotatedString {
        append(text)

        val startIndex = text.indexOf("2")
        val style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        addStyle(style, startIndex, startIndex + 1)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Insights,
            modifier = Modifier.fillMaxHeight(0.4f).aspectRatio(1f),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "Line chart icon"
        )

        Text(
            text = message,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun MoodAxisDivider(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        label = "Spacer Animation"
    )
    Spacer(
        modifier
            .width(strokeWidth)
            .fillMaxHeight()
            .alpha(animatedAlpha)
            .background(color, CircleShape)
    )
}

@Composable
private fun ChartBackground(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Canvas(modifier) {
        val gapHeight = (size.height / 5)

        (0..4).forEach { line ->
            drawLine(
                color = color,
                start = Offset(0f, line * gapHeight),
                end = Offset(size.width, line * gapHeight),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                    phase = 0f
                )
            )
        }

        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun AdoptedChartLine(
    data: Map<Int, MoodType?>,
    chartFrame: ChartFrame,
    modifier: Modifier
) {
    BoxWithConstraints(modifier) {
        ChartLine(
            data = data,
            chartFrame = chartFrame,
            modifier = Modifier
                .height(maxHeight)
                .width(
                    maxOf(minWidth, getTitleWidth(chartFrame) * data.keys.size)
                )
        )
    }
}

@Composable
private fun ChartLine(
    data: Map<Int, MoodType?>,
    chartFrame: ChartFrame,
    modifier: Modifier,
    lineWidth: Dp = 2.dp,
    lineCap: StrokeCap = StrokeCap.Round
) {
    val drawProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        drawProgress.snapTo(0f)
        val duration = getAnimDuration(chartFrame, data.values)
        drawProgress.animateTo(1f, tween(duration, easing = LinearEasing))
    }

    Canvas(modifier) {
        val coordinates = mutableListOf<PointF>()
        val controlPoints1 = mutableListOf<PointF>()
        val controlPoints2 = mutableListOf<PointF>()

        val width = this.size.width
        val height = this.size.height

        val padding = getTitleWidth(chartFrame).toPx() / 2
        val xAxisSpace = ((width - padding * 2) / (data.size - 1))
        val yAxisSpace = (height / 5)
        val minKey = data.keys.min()

        data.forEach { (key, mood) ->
            if (mood != null) {
                val normalizedIndex = (key - minKey)
                val x = (xAxisSpace * normalizedIndex) + padding
                val y = height - (yAxisSpace * mood.chartWeight) - (yAxisSpace / 2)
                coordinates.add(PointF(x,y))
            }
        }

        for (i in 1 until coordinates.size) {
            val diff = coordinates[i].x - coordinates[i - 1].x
            val leverage = diff * 0.7f

            controlPoints1.add(PointF(coordinates[i - 1].x + leverage, coordinates[i - 1].y))
            controlPoints2.add(PointF(coordinates[i].x - leverage, coordinates[i].y))
        }

        val chartPath = Path().apply {
            moveTo(coordinates.first().x, coordinates.first().y)
            for (i in 0 until coordinates.size - 1) {
                cubicTo(
                    controlPoints1[i].x,controlPoints1[i].y,
                    controlPoints2[i].x,controlPoints2[i].y,
                    coordinates[i + 1].x,coordinates[i + 1].y
                )
            }
        }

        val pathMeasure = PathMeasure().apply { setPath(chartPath, false) }
        val pathSegment = Path()
        val stopDistance = drawProgress.value * pathMeasure.length
        pathMeasure.getSegment(0f, stopDistance, pathSegment, true)

        val endPoint = pathMeasure.getPosition(stopDistance)
        val startPoint = pathMeasure.getPosition(0f)

        val fillPath = android.graphics.Path(pathSegment.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(endPoint.x, height)
                lineTo(startPoint.x, height)
                close()
            }

        val highestMood = data.values.filterNotNull().maxBy(MoodType::chartWeight)

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                0f to highestMood.color.copy(0.64f),
                1f to Color.Transparent,
                startY = height - (yAxisSpace * highestMood.chartWeight)
            )
        )

        val chartGradient = getChartGradient()
        drawPath(
            path = pathSegment,
            brush = chartGradient,
            style = Stroke(
                width = lineWidth.toPx(),
                cap = lineCap
            )
        )

        listOf(startPoint, endPoint).forEach { offset ->
            drawCircle(
                brush = chartGradient,
                radius = (lineWidth + 0.5.dp).toPx(),
                center = offset
            )
        }
    }
}

private fun getAnimDuration(chartFrame: ChartFrame, values: Collection<MoodType?>): Int {
    val itemsToDraw = values.count { it != null }
    return when (chartFrame) {
        ChartFrame.MONTH -> (MONTH_MAX_DURATION / values.size) * itemsToDraw
        ChartFrame.WEEK -> (WEEK_MAX_DURATION / values.size) * itemsToDraw
    }
}

private const val WEEK_MAX_DURATION = 2000
private const val MONTH_MAX_DURATION = 5000

private fun getChartGradient(step: Float = 0.04f) = Brush.verticalGradient(
    0f to MoodType.Rad.color,
    0.2f - step to MoodType.Rad.color,

    0.2f + step to MoodType.Good.color,
    0.4f - step to MoodType.Good.color,

    0.4f + step to MoodType.Meh.color,
    0.6f - step to MoodType.Meh.color,

    0.6f + step to MoodType.Bad.color,
    0.8f - step to MoodType.Bad.color,

    0.8f + step to MoodType.Awful.color,
    1f to MoodType.Awful.color
)

private val MoodType.chartWeight: Int get() {
    return when (this) {
        MoodType.Awful -> 0
        MoodType.Bad -> 1
        MoodType.Meh -> 2
        MoodType.Good -> 3
        MoodType.Rad -> 4
    }
}

@Composable
private fun DayAxis(
    days: Set<Int>,
    chartFrame: ChartFrame,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            DayTitle(day, chartFrame)
        }
    }
}

@Composable
private fun DayTitle(day: Int, chartFrame: ChartFrame) {
    val weekDays = stringArrayResource(R.array.week_days)
    val validWeekDay = (day - 1) in weekDays.indices
    val text = when {
        chartFrame == ChartFrame.WEEK && validWeekDay -> weekDays[day - 1]
        else -> day.toString()
    }
    Text(
        text = text,
        fontSize = 12.sp,
        maxLines = 1,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .height(16.dp)
            .width(getTitleWidth(chartFrame))
    )
}

@Composable
private fun MoodAxis(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MoodType.values.forEach { mood ->
            MoodIcon(
                mood.animationRes,
                Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
private fun MoodIcon(
    @RawRes resource: Int,
    modifier: Modifier = Modifier
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(resource))

    LottieAnimation(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        modifier = modifier
    )
}

private fun getTitleWidth(chartFrame: ChartFrame): Dp {
    return when (chartFrame) {
        ChartFrame.MONTH -> 20.dp
        ChartFrame.WEEK -> 32.dp
    }
}