package com.kappdev.moodtracker.domain.model

import android.content.Context
import com.kappdev.moodtracker.R

data class QuoteBlock(
    val onCalendarScreen: Boolean = true,
    val onChartScreen: Boolean = true
)

fun QuoteBlock.getTitle(context: Context): String = with(context) {
    return when {
        onCalendarScreen && onChartScreen -> getString(R.string.quote_block_calendar_and_chart)
        onCalendarScreen -> getString(R.string.quote_block_calendar)
        onChartScreen -> getString(R.string.quote_block_chart)
        else -> getString(R.string.disabled)
    }
}