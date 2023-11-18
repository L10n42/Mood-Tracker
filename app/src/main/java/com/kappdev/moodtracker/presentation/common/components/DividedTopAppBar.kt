package com.kappdev.moodtracker.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DividedContent(
    isDividerVisible: Boolean = true,
    divider: @Composable () -> Unit = { Divider() },
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
        if (isDividerVisible) {
            divider()
        }
    }
}