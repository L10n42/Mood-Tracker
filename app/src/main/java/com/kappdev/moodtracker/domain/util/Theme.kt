package com.kappdev.moodtracker.domain.util

import androidx.annotation.StringRes
import com.kappdev.moodtracker.R

enum class Theme(@StringRes val titleRes: Int) {
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
    SYSTEM_DEFAULT(R.string.theme_system_default)
}