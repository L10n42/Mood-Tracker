package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.convexEffect(
    shape: Shape,
    intensity: Dp = 2.dp
) = this.innerShadow(
    color = Color.White.copy(0.72f),
    shape = shape,
    offsetY = intensity,
    offsetX = intensity
).innerShadow(
    color = Color.Black.copy(0.54f),
    shape = shape,
    offsetY = -intensity,
    offsetX = -intensity
)