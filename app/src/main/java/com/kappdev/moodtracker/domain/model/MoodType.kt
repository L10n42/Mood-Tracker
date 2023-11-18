package com.kappdev.moodtracker.domain.model

import androidx.annotation.RawRes
import androidx.compose.ui.graphics.Color
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.ui.theme.AwfulColor
import com.kappdev.moodtracker.ui.theme.BadColor
import com.kappdev.moodtracker.ui.theme.GoodColor
import com.kappdev.moodtracker.ui.theme.MehColor
import com.kappdev.moodtracker.ui.theme.RadColor

sealed class MoodType(val key: String, val color: Color, @RawRes val animationRes: Int) {
    data object Rad: MoodType(KEY_RAD, RadColor, R.raw.rad_anim)
    data object Good: MoodType(KEY_GOOD, GoodColor, R.raw.good_anim)
    data object Meh: MoodType(KEY_MEH, MehColor, R.raw.meh_anim)
    data object Bad: MoodType(KEY_BAD, BadColor, R.raw.bad_anim)
    data object Awful: MoodType(KEY_AWFUL, AwfulColor, R.raw.awful_anim)

    companion object {
        const val KEY_RAD = "RAD"
        const val KEY_GOOD = "GOOD"
        const val KEY_MEH = "MEH"
        const val KEY_BAD = "BAD"
        const val KEY_AWFUL = "AWFUL"
    }
}

fun MoodType.Companion.fromKey(key: String): MoodType {
    return when (key) {
        KEY_RAD -> MoodType.Rad
        KEY_GOOD -> MoodType.Good
        KEY_MEH -> MoodType.Meh
        KEY_BAD -> MoodType.Bad
        KEY_AWFUL -> MoodType.Awful
        else -> throw IllegalArgumentException("No such a type with key = $key")
    }
}

val MoodType.Companion.values: List<MoodType>
    get() = listOf(MoodType.Rad, MoodType.Good, MoodType.Meh, MoodType.Bad, MoodType.Awful)