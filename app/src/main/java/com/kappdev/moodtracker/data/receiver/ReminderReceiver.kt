package com.kappdev.moodtracker.data.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kappdev.moodtracker.MainActivity
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var settings: SettingsManager

    override fun onReceive(context: Context, intent: Intent?) {
        sendReminderNotification(context)

        CoroutineScope(Dispatchers.IO).launch {
            val remainder = settings.getValueFlow(Settings.Reminder).first()
            if (remainder.enabled) {
                reminderManager.setRemainder(remainder.time)
            }
        }
    }

    private fun sendReminderNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        contentIntent.putExtra(IS_REMINDER_INTENT, true)
        val pendingIntent = PendingIntent.getActivity(context, ACTIVITY_REQUEST_CODE, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle("Remainder")
            setContentText("How do your feel today?")
            setAutoCancel(true)
            setCategory(NotificationCompat.CATEGORY_REMINDER)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setSmallIcon(R.drawable.ic_emoji_emotions)
            setContentIntent(pendingIntent)
            setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            setDefaults(NotificationCompat.DEFAULT_SOUND)
            setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val IS_REMINDER_INTENT = "REMINDER_INTENT"
        const val NOTIFICATION_CHANNEL_ID = "REMINDER_NOTIFICATION_ID"
        const val NOTIFICATION_CHANNEL_NAME = "REMINDER_NOTIFICATION"
        const val ACTIVITY_REQUEST_CODE = 1237

        private const val NOTIFICATION_ID = 320750
    }
}