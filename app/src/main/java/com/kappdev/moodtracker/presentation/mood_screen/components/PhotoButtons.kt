package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.common.components.HorizontalSpace

@Composable
fun PhotoButtons(
    modifier: Modifier = Modifier,
    onTakePhoto: () -> Unit,
    onFromGallery: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val buttonModifier = Modifier.weight(1f)
        PhotoButton(
            icon = Icons.Rounded.PhotoCamera,
            text = stringResource(R.string.btn_take_photo),
            onClick = onTakePhoto,
            modifier = buttonModifier
        )

        HorizontalSpace(16.dp)

        PhotoButton(
            icon = Icons.Rounded.Image,
            text = stringResource(R.string.btn_from_gallery),
            onClick = onFromGallery,
            modifier = buttonModifier
        )
    }
}

@Composable
private fun PhotoButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        HorizontalSpace(space = 8.dp)

        Text(
            text = text,
            maxLines = 1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis
        )
    }
}