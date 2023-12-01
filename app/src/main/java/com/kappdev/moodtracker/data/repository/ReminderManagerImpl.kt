package com.kappdev.moodtracker.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.data.receiver.ReminderReceiver
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.util.Result
import com.kappdev.moodtracker.domain.util.fail
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

class ReminderManagerImpl @Inject constructor(
    private val context: Context
): ReminderManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setRemainder(time: LocalTime): Result<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return Result.fail(context.getString(R.string.permission_not_granted_msg))
        }
        val intent = Intent(context, ReminderReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, REMINDER_ID, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTime(time), intent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTime(time), intent)
        }
        return Result.Success(context.getString(R.string.reminder_set_msg))
    }

    override fun stopRemainder() {
        val intent = Intent(context, ReminderReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, REMINDER_ID, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.cancel(intent)
    }

    private fun getTime(time: LocalTime): Long {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)

            if (time.isAfter(LocalTime.now())) {
                add(Calendar.DAY_OF_YEAR, 0)
            } else {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }.timeInMillis
    }


    companion object {
        private const val REMINDER_ID = 3264
    }
}