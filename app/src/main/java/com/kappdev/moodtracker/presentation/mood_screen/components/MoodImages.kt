package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Image
import com.kappdev.moodtracker.presentation.common.components.CustomAlertDialog
import com.kappdev.moodtracker.presentation.common.rememberMutableDialogState

@Composable
fun MoodImages(
    images: List<Image>,
    modifier: Modifier = Modifier,
    removeImage: (image: Image) -> Unit,
) {
    val deleteImageDialog = rememberMutableDialogState<Image?>(initialData = null)
    val overviewImageDialog = rememberMutableDialogState<Image?>(initialData = null)

    if (overviewImageDialog.isVisible.value) {
        OverviewImageDialog(
            image = overviewImageDialog.dialogData.value,
            onDismiss = overviewImageDialog::hideDialog
        )
    }

    if (deleteImageDialog.isVisible.value) {
        DeleteImageDialog(
            onDismiss = deleteImageDialog::hideDialog,
            onRemove = {
                deleteImageDialog.dialogData.value?.let(removeImage)
            },
        )
    }

    if (images.isNotEmpty()) {
        ImagesGrid(
            minWidth = 150.dp,
            modifier = modifier,
            itemsPadding = 16.dp
        ) {
            images.forEach { image ->
                ImageCard(
                    image = image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    onRemove = { deleteImageDialog.showDialog(image) },
                    onOverview = { overviewImageDialog.showDialog(image) }
                )
            }
        }
    }
}

@Composable
private fun DeleteImageDialog(
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
) {
    CustomAlertDialog(
        title = stringResource(R.string.delete_photo_title),
        text = stringResource(R.string.delete_photo_msg),
        confirmText = stringResource(R.string.btn_delete),
        cancelText = stringResource(R.string.btn_keep),
        onDismiss = onDismiss,
        onConfirm = onRemove
    )
}

@Composable
private fun ImageCard(
    image: Image,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    onRemove: () -> Unit,
    onOverview: () -> Unit
) {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface, shape)
    ) {
        SubcomposeAsyncImage(
            model = image.model,
            contentDescription = null,
            modifier = modifier.clip(shape).clickable(onClick = onOverview),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator()
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

        IconButton(
            onClick = onRemove,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .padding(8.dp)
                .size(18.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Delete image icon",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}