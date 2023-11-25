package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kappdev.moodtracker.R

@Composable
fun CustomAlertDialog(
    title: String? = null,
    text: String? = null,
    confirmText: String = stringResource(R.string.btn_confirm),
    cancelText: String = stringResource(R.string.btn_cancel),
    dismissAfterClick: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = onDismiss
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = DialogShape) {
            Column(
                modifier = Modifier.padding(ContentPadding)
            ) {
                title?.let {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                VerticalSpace(8.dp)

                text?.let {
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                VerticalSpace(16.dp)

                ContentDivider()
                CustomDialogButtons(
                    confirmText = confirmText,
                    cancelText = cancelText,
                    onConfirm = {
                        if (dismissAfterClick) onDismiss()
                        onConfirm()
                    },
                    onCancel = {
                        if (dismissAfterClick) onDismiss()
                        onCancel()
                    }
                )
            }
        }
    }
}

@Composable
fun CustomDialog(
    confirmText: String = stringResource(R.string.btn_confirm),
    cancelText: String = stringResource(R.string.btn_cancel),
    dismissAfterClick: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = DialogShape) {
            Column(
                modifier = Modifier.padding(ContentPadding)
            ) {
                content()
                ContentDivider()
                CustomDialogButtons(
                    confirmText = confirmText,
                    cancelText = cancelText,
                    onConfirm = {
                        if (dismissAfterClick) onDismiss()
                        onConfirm()
                    },
                    onCancel = {
                        if (dismissAfterClick) onDismiss()
                        onCancel()
                    }
                )
            }
        }
    }
}

@Composable
private fun CustomDialogButtons(
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomDialogButton(
            text = cancelText,
            modifier = Modifier.weight(1f),
            onClick = onCancel
        )
        ButtonsDivider()
        CustomDialogButton(
            text = confirmText,
            modifier = Modifier.weight(1f),
            onClick = onConfirm
        )
    }
}

@Composable
private fun ContentDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(DividerAlpha),
                shape = CircleShape
            )
    )
}

@Composable
private fun ButtonsDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(ButtonHeight)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(DividerAlpha),
                shape = CircleShape
            )
    )
}

@Composable
private fun CustomDialogButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val pressShadowAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.54f else 0f,
        label = "pressed button shadow alpha"
    )

    val pressTextAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.72f else 1f,
        label = "pressed button text alpha"
    )

    Box(
        modifier = modifier
            .height(ButtonHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .innerShadow(
                color = Color.Black.copy(pressShadowAlpha),
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary.copy(pressTextAlpha),
            fontWeight = FontWeight.SemiBold
        )
    }
}

private val ButtonHeight = 40.dp
private val DialogShape = RoundedCornerShape(16.dp)
private val ContentPadding = PaddingValues(16.dp)
private const val DividerAlpha = 0.16f