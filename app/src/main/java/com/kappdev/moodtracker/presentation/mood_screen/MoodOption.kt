package com.kappdev.moodtracker.presentation.mood_screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappdev.moodtracker.R

sealed class MoodOption(val icon: ImageVector, @StringRes val titleRes: Int) {
    data object CopyNote : MoodOption(Icons.Rounded.ContentCopy, R.string.copy_note)
    data object Delete : MoodOption(Icons.Rounded.Delete, R.string.delete)

    companion object
}

val MoodOption.Companion.entries: List<MoodOption>
    get() = listOf(
        MoodOption.Delete,
        MoodOption.CopyNote
    )
