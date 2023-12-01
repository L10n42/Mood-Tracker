package com.kappdev.moodtracker.domain.repository

import com.kappdev.moodtracker.domain.util.Result
import java.time.LocalTime

interface ReminderManager {

    fun setRemainder(time: LocalTime): Result<String>

    fun stopRemainder()

}