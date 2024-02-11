package com.kappdev.moodtracker.presentation.mood_chart

import androidx.annotation.StringRes
import com.kappdev.moodtracker.R

enum class ChartFrame(@StringRes val titleRes: Int) {
    MONTH(R.string.month_title),
    WEEK(R.string.week_title)
}