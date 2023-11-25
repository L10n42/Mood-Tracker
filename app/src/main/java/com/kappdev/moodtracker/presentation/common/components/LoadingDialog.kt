package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kappdev.moodtracker.presentation.common.DialogState

@Composable
fun LoadingDialog(
    state: DialogState<String?>
) {
    if (state.isVisible.value) {
        LoadingDialog(state.dialogData.value)
    }
}

@Composable
fun LoadingDialog(
    message: String? = null
) {
    Dialog(
        onDismissRequest = { /* Nothing */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round
                )

                message?.let {
                    HorizontalSpace(16.dp)
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}