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
import com.kappdev.moodtracker.domain.model.QuoteBlock
import com.kappdev.moodtracker.presentation.common.MutableDialogState
import com.kappdev.moodtracker.presentation.common.components.CustomDialog
import com.kappdev.moodtracker.presentation.common.components.TitledCheckBox

@Composable
fun QuoteBlockDialog(
    state: MutableDialogState<QuoteBlock>,
    onConfirm: (QuoteBlock) -> Unit
) {
    if (state.isVisible.value) {
        QuoteBlockDialog(
            initialQuoteBlock = state.dialogData.value,
            onDismiss = state::hideDialog,
            onConfirm = onConfirm
        )
    }
}

@Composable
fun QuoteBlockDialog(
    initialQuoteBlock: QuoteBlock,
    onDismiss: () -> Unit,
    onCancel: () -> Unit = onDismiss,
    onConfirm: (QuoteBlock) -> Unit,
) {
    var selected by remember { mutableStateOf(initialQuoteBlock) }

    CustomDialog(
        confirmText = stringResource(R.string.btn_save),
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(selected)
        },
        onCancel = onCancel
    ) {
        Column {
            Text(
                text = stringResource(R.string.display_quote_block),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            TitledCheckBox(
                title = stringResource(R.string.quote_block_calendar),
                checked = selected.onCalendarScreen,
                onCheckedChange = {
                    selected = selected.copy(onCalendarScreen = it)
                }
            )

            TitledCheckBox(
                title = stringResource(R.string.quote_block_chart),
                checked = selected.onChartScreen,
                onCheckedChange = {
                    selected = selected.copy(onChartScreen = it)
                }
            )
        }
    }
}