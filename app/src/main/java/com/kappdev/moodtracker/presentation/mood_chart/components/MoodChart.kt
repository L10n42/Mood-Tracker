package com.kappdev.moodtracker.presentation.mood_chart.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kappdev.moodtracker.domain.model.MoodType

val DebugItems = mapOf(
    "Mon" to MoodType.Rad,
    "Tue" to MoodType.Good,
    "Wed" to MoodType.Meh,
    "Thu" to MoodType.Meh,
    "Fri" to MoodType.Bad,
    "Sat" to MoodType.Rad,
    "Sun" to MoodType.Awful
)

@Composable
fun MoodChart(
    modifier: Modifier = Modifier
) {

    Row(
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
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DebugItems.forEach { entry ->
            ChartBar(
                label = entry.key,
                mood = entry.value,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun ChartBar(
    label: String,
    mood: MoodType,
    modifier: Modifier = Modifier
) {

    val animatedHeight by animateFloatAsState(
        targetValue = mood.getHeightFraction(),
        label = "animated height"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
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