package com.kappdev.moodtracker.presentation.mood_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.kappdev.moodtracker.domain.util.getMonthName
import com.kappdev.moodtracker.domain.util.isCurrentYear
import com.kappdev.moodtracker.presentation.mood_screen.MoodOption
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTopBar(
    date: LocalDate?,
    onOptionClick: (MoodOption) -> Unit,
    onBack: () -> Unit
) {
    var isOptionsVisible by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            date?.let {
                Text(
                    text = styleDate(date),
                    maxLines = 1,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { isOptionsVisible = true }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Options"
                )
            }
            MoodOptionsPopup(
                isVisible = isOptionsVisible,
                onClick = onOptionClick,
                onDismiss = {
                    isOptionsVisible = false
                }
            )
        }
    )
}

private fun styleDate(date: LocalDate): String {
    return buildString {
        append(date.getMonthName())
        append(" ")
        append(date.dayOfMonth)

        if (!date.isCurrentYear()) {
            append(", ")
            append(date.year)
        }
    }
}