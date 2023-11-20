package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.model.values

@Composable
fun MoodPicker(
    selected: MoodType?,
    onSelect: (new: MoodType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MoodType.values.forEach { moodType ->
            MoodButton(
                type = moodType,
                selected = (moodType.key == selected?.key),
                onClick = {
                    onSelect(moodType)
                }
            )
        }
    }
}

@Composable
private fun RowScope.MoodButton(
    type: MoodType,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(type.animationRes))

    val emojiSizeFraction by animateFloatAsState(
        targetValue = if (selected) 1f else 0.9f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "emoji size animation"
    )

    val glowScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
        label = "glow scale animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(glowScale)
                .background(
                    color = type.color.copy(0.5f),
                    shape = CircleShape
                )
        )

        LottieAnimation(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop,
            isPlaying = true,
            modifier = Modifier.fillMaxSize(emojiSizeFraction)
        )
    }
}

