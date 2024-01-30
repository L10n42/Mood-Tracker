package com.kappdev.moodtracker.presentation.options

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.domain.model.Reminder
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.use_case.RateTheApp
import com.kappdev.moodtracker.domain.util.Result
import com.kappdev.moodtracker.domain.util.Toaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val reminderManager: ReminderManager,
    private val toaster: Toaster,
    private val app: Application,
    val rateTheApp: RateTheApp
) : ViewModel() {

    private var _turnOffReminder = MutableSharedFlow<Unit>()
    val turnOffReminder: SharedFlow<Unit> = _turnOffReminder

    fun updateRemainder(reminder: Reminder) {
        if (reminder.enabled) {
            scheduleRemainder(reminder.time)
        } else {
            reminderManager.stopRemainder()
        }
    }

    private fun scheduleRemainder(time: LocalTime) {
        val result = reminderManager.setRemainder(time)
        when (result) {
            is Result.Success -> toaster.show(result.value)
            is Result.Failure -> {
                result.exception.message?.let(toaster::show)
                turnOffReminder()
            }
        }
    }

    private fun turnOffReminder() {
        viewModelScope.launch {
            _turnOffReminder.emit(Unit)
        }
    }

    fun needAlarmPermission(): Boolean {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()
    }
}