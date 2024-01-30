package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kappdev.moodtracker.presentation.mood_screen.MoodOption
import com.kappdev.moodtracker.presentation.mood_screen.entries

@Composable
fun MoodOptionsPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onClick: (MoodOption) -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = isVisible

    if (expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
        Popup(
            onDismissRequest = onDismiss,
            alignment = Alignment.TopStart,
            properties = PopupProperties(focusable = true)
        ) {
            AnimatedVisibility(
                visibleState = expandedState,
                enter = scaleIn(
                    initialScale = 0.3f,
                    transformOrigin = TransformOrigin(1f, 0f)
                ) + fadeIn(),
                exit = scaleOut(
                    targetScale = 0.3f,
                    transformOrigin = TransformOrigin(1f, 0f)
                ) + fadeOut()
            ) {
                PopupContent(onClick = onClick)
            }
        }
    }
}

@Composable
fun PopupContent(
    onClick: (MoodOption) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 12.dp,
        modifier = Modifier.width(150.dp)
    ) {
        Column {
            MoodOption.entries.forEachIndexed { index, option ->
                PopupMenuItem(option, onClick)

                if (index != MoodOption.entries.lastIndex) {
                    PopupMenuDivider()
                }
            }
        }
    }
}

@Composable
private fun PopupMenuDivider() {
    Divider(
        Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(0.16f)
    )
}

@Composable
private fun PopupMenuItem(
    option: MoodOption,
    onClick: (MoodOption) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(option) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(option.titleRes),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}