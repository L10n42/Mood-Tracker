package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R

@Composable
fun NoteField(
    note: String,
    modifier: Modifier = Modifier,
    onNoteChange: (new: String) -> Unit
) {
    BasicTextField(
        value = note,
        modifier = modifier,
        onValueChange = onNoteChange,
        minLines = 8,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = Brush.linearGradient(
            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary)
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp)
                    .dashedBorder(
                        color = MaterialTheme.colorScheme.primary,
                        cornerRadiusDp = 12.dp
                    )
                    .padding(8.dp),
                content = {
                    Placeholder(
                        isPlaceholderVisible = note.isEmpty(),
                        textField = innerTextField
                    )
                }
            )
        }
    )
}

@Composable
private fun Placeholder(
    isPlaceholderVisible: Boolean,
    textField: @Composable () -> Unit
) {
    Box {
        if (isPlaceholderVisible) {
            Text(
                text = stringResource(R.string.note_hint),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        }
        textField()
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadiusDp: Dp,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = composed {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val cornerRadiusPx = with(density) { cornerRadiusDp.toPx() }
    val dashWidthPx = with(density) { dashWidth.toPx() }

    this.then(
        Modifier.drawBehind {
            val stroke = Stroke(
                cap = cap,
                width = strokeWidthPx,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidthPx, dashWidthPx), 0f)
            )

            drawRoundRect(
                color = color,
                style = stroke,
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    )
}