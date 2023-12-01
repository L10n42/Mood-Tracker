package com.kappdev.moodtracker.presentation.options.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.kappdev.moodtracker.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun TimePicker(
    initTime: LocalTime,
    state: MaterialDialogState,
    closePicker: () -> Unit,
    onTimeSelect: (time: LocalTime) -> Unit
) {
    var time by remember { mutableStateOf(initTime) }

    LaunchedEffect(initTime) {
        time = initTime
    }

    MaterialDialog(
        dialogState = state,
        backgroundColor = MaterialTheme.colorScheme.surface,
        onCloseRequest = { closePicker() },
        buttons = {
            positiveButton(
                text = stringResource(R.string.btn_select),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                onClick = { onTimeSelect(time) }
            )
            negativeButton(
                text = stringResource(R.string.btn_cancel),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                onClick = closePicker
            )
        }
    ) {
        timepicker(
            initialTime = time,
            is24HourClock = true,
            onTimeChange = { time = it },
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary,
                activeTextColor = MaterialTheme.colorScheme.onSurface,
                inactiveTextColor = MaterialTheme.colorScheme.onSurface,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                selectorColor = MaterialTheme.colorScheme.primary,
                borderColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}