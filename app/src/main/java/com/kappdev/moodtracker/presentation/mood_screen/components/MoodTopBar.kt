package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTopBar(
    date: LocalDate?,
    onBack: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                text = date?.toString() ?: "",
                maxLines = 1,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        }
    )
}