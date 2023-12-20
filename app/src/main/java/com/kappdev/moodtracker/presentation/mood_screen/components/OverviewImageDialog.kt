package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.kappdev.moodtracker.domain.model.Image

@Composable
fun OverviewImageDialog(
    image: Image?,
    onShare: (image: Image) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        ) {
            SubcomposeAsyncImage(
                model = image?.model,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(42.dp)
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.Rounded.ErrorOutline,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Image loading error icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    icon = Icons.Rounded.Close,
                    onClick = onDismiss
                )

                ActionButton(
                    icon = Icons.Rounded.Share,
                    onClick = {
                        image?.let(onShare)
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}