package com.kappdev.moodtracker.presentation.options.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.common.components.CustomAlertDialog

@Composable
fun AlarmPermissionDialog(
    onDismiss: () -> Unit,
    onGrant: () -> Unit
) {
    CustomAlertDialog(
        title = stringResource(R.string.permission_required),
        text = stringResource(R.string.permission_required_msg),
        confirmText = stringResource(R.string.btn_grant),
        cancelText = stringResource(R.string.btn_deny),
        onDismiss = onDismiss,
        onConfirm = onGrant
    )
}