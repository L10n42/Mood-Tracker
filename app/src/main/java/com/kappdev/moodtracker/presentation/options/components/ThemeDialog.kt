package com.kappdev.moodtracker.presentation.options.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.common.DialogState
import com.kappdev.moodtracker.presentation.common.components.CustomDialog

@Composable
fun ThemeDialog(
    state: DialogState<Theme>,
    onDismiss: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    onConfirm: (Theme) -> Unit
) {
    if (state.isVisible.value) {
        ThemeDialog(
            initialTheme = state.dialogData.value,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            onCancel = onCancel
        )
    }
}

@Composable
fun ThemeDialog(
    initialTheme: Theme,
    onDismiss: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    onConfirm: (Theme) -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(initialTheme) }

    CustomDialog(
        confirmText = stringResource(R.string.btn_save),
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(selectedTheme)
        },
        onCancel = onCancel
    ) {
        Column {
            Text(
                text = "Select color mode",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Theme.values().forEach { theme ->
                TitledRadioButton(
                    title = stringResource(theme.titleRes),
                    selected = (theme == selectedTheme),
                    onClick = { selectedTheme = theme }
                )
            }
        }
    }
}

@Composable
private fun TitledRadioButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(8.dp)
        )
    }
}