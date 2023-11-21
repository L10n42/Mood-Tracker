package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.presentation.mood_chart.ChartType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodChart(
    data: Map<Int, MoodType?>,
    chartType: ChartType,
    modifier: Modifier = Modifier
) {
    val weekDays = stringArrayResource(R.array.week_days)
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LazyRow(
        state = listState,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .chartBackground(
                labelHeight = LabelHeight,
                color = MaterialTheme.colorScheme.onBackground
            ),
        flingBehavior = snapBehavior,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data.toList()) { (day, mood) ->
            val label = when (chartType) {
                ChartType.MONTH -> day.toString()
                ChartType.WEEK -> weekDays[day - 1]
            }
            ChartBar(
                mood = mood,
                label = label,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(32.dp)
            )
        }
    }
}

@Composable
private fun ChartBar(
    label: String,
    mood: MoodType?,
    modifier: Modifier = Modifier
) {
    var heightTarget by remember { mutableFloatStateOf(0f) }

    val animatedHeight by animateFloatAsState(
        targetValue = heightTarget,
        animationSpec = tween(500),
        label = "animated height"
    )

    LaunchedEffect(mood) {
        heightTarget = mood?.getHeightFraction() ?: 0f
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        mood?.let {
            Box(
                modifier = Modifier
                    .padding(bottom = LabelHeight)
                    .fillMaxWidth()
                    .fillMaxHeight(animatedHeight)
                    .background(mood.color.copy(0.8f), CircleShape)
            ) {
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(mood.animationRes))

                LottieAnimation(
                    composition = lottieComposition,
                    iterations = LottieConstants.IterateForever,
                    isPlaying = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }

        BarLabel(
            text = label,
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BarLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Light,
        modifier = modifier
    )
}

private fun MoodType.getHeightFraction(): Float {
    return when (this) {
        MoodType.Rad -> 1f
        MoodType.Good -> 0.8f
        MoodType.Meh -> 0.6f
        MoodType.Bad -> 0.4f
        MoodType.Awful -> 0.2f
    }
}

private val LabelHeight = 16.dp