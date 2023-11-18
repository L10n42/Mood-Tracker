package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R

@Composable
fun DoneButton(
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val animatedBtnScale by animateFloatAsState(
        targetValue = if (pressed) 1.2f else 1f,
        label = "animated button scale"
    )

    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        ),
        modifier = Modifier
            .height(50.dp)
            .width(180.dp)
            .scale(animatedBtnScale),
        interactionSource = interactionSource
    ) {
        Text(
            text = stringResource(R.string.btn_done),
            maxLines = 1,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis
        )
    }
}