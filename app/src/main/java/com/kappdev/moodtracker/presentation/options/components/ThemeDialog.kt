package com.kappdev.moodtracker.presentation.options.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.common.DialogState
import com.kappdev.moodtracker.presentation.common.components.CustomDialog
import com.kappdev.moodtracker.presentation.common.components.TitledRadioButton

@Composable
fun ThemeDialog(
    state: DialogState<Theme>,
    onDismiss: () -> Unit,
    onConfirm: (Theme) -> Unit
) {
    if (state.isVisible.value) {
        ThemeDialog(
            initialTheme = state.dialogData.value,
            onConfirm = onConfirm,
            onDismiss = onDismiss
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
                text = stringResource(R.string.select_color_mode),
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

