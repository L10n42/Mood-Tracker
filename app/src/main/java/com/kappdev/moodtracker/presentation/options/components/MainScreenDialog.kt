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
import com.kappdev.moodtracker.domain.util.MainScreen
import com.kappdev.moodtracker.presentation.common.DialogState
import com.kappdev.moodtracker.presentation.common.components.CustomDialog
import com.kappdev.moodtracker.presentation.common.components.TitledRadioButton

@Composable
fun MainScreenDialog(
    state: DialogState<MainScreen>,
    onDismiss: () -> Unit,
    onConfirm: (MainScreen) -> Unit
) {
    if (state.isVisible.value) {
        MainScreenDialog(
            initialMainScreen = state.dialogData.value,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}


@Composable
fun MainScreenDialog(
    initialMainScreen: MainScreen,
    onDismiss: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    onConfirm: (MainScreen) -> Unit,
) {
    var selectedMainScreen by remember { mutableStateOf(initialMainScreen) }

    CustomDialog(
        confirmText = stringResource(R.string.btn_save),
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(selectedMainScreen)
        },
        onCancel = onCancel
    ) {
        Column {
            Text(
                text = stringResource(R.string.select_main_screen),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            MainScreen.values().forEach { mainScreen ->
                TitledRadioButton(
                    title = stringResource(mainScreen.titleRes),
                    selected = (mainScreen == selectedMainScreen),
                    onClick = { selectedMainScreen = mainScreen }
                )
            }
        }
    }
}