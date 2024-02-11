package com.kappdev.moodtracker.presentation.mood_chart

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappdev.moodtracker.R

enum class ChartType(@StringRes val titleRes: Int, val icon: ImageVector) {
    LINE(R.string.chart_type_line, Icons.Rounded.Insights),
    BARS(R.string.chart_type_bars, Icons.Rounded.BarChart)
}