package com.kappdev.moodtracker.domain.util

import androidx.annotation.StringRes
import com.kappdev.moodtracker.R

enum class Theme(@StringRes val titleRes: Int) {
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
    SYSTEM_DEFAULT(R.string.theme_system_default)
}

class ThemeTransformer: SettingsTransformer<Theme, String> {
    override fun serialize(value: Theme): String {
        return value.name
    }

    override fun deserialize(name: String): Theme {
        return Theme.valueOf(name)
    }
}