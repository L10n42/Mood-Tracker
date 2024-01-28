package com.kappdev.moodtracker.data.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.kappdev.moodtracker.MainActivity
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.use_case.GetMoodByDate
import com.kappdev.moodtracker.domain.util.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var settings: SettingsManager

    @Inject
    lateinit var getMoodByDate: GetMoodByDate

    override fun onReceive(context: Context, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val todayMood = getMoodByDate(LocalDate.now())
            if (todayMood == null) {
                sendReminderNotification(context)
            }

            val remainder = settings.getValueFlow(Settings.Reminder).first()
            if (remainder.enabled) {
                reminderManager.setRemainder(remainder.time)
            }
        }
    }

    private fun sendReminderNotification(context: Context) {
        val openAppIntent = Intent(context, MainActivity::class.java)
        openAppIntent.putExtra(IS_REMINDER_INTENT, true)
        val openAppAction = PendingIntent.getActivity(context, ACTIVITY_REQUEST_CODE, openAppIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationLayout = RemoteViews(context.packageName, R.layout.reminder_layout)
        notificationLayout.setOnClickPendingIntent(R.id.btnOpenApp, openAppAction)

        with(context) {
            notificationLayout.setOnClickPendingIntent(R.id.rad, quickMoodIntent(MoodType.Rad))
            notificationLayout.setOnClickPendingIntent(R.id.good, quickMoodIntent(MoodType.Good))
            notificationLayout.setOnClickPendingIntent(R.id.meh, quickMoodIntent(MoodType.Meh))
            notificationLayout.setOnClickPendingIntent(R.id.bad, quickMoodIntent(MoodType.Bad))
            notificationLayout.setOnClickPendingIntent(R.id.awful, quickMoodIntent(MoodType.Awful))
        }

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle(context.getString(R.string.today_mood_question))
            setCustomBigContentView(notificationLayout)
            setAutoCancel(true)
            setCategory(NotificationCompat.CATEGORY_REMINDER)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setSmallIcon(R.drawable.ic_emoji_emotions)
            setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            setDefaults(NotificationCompat.DEFAULT_SOUND)
            setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun Context.quickMoodIntent(type: MoodType): PendingIntent {
        val quickMoodIntent = Intent(this, QuickMoodReceiver::class.java)
        quickMoodIntent.action = QuickMoodReceiver.QUICK_MOOD_ACTION
        quickMoodIntent.putExtra(QuickMoodReceiver.MOOD_TYPE_EXTRA, type.key)

        return PendingIntent.getBroadcast(this, type.hashCode(), quickMoodIntent, PendingIntent.FLAG_MUTABLE)
    }

    companion object {
        const val IS_REMINDER_INTENT = "REMINDER_INTENT"
        const val NOTIFICATION_CHANNEL_ID = "REMINDER_NOTIFICATION_ID"
        const val NOTIFICATION_CHANNEL_NAME = "REMINDER_NOTIFICATION"
        const val ACTIVITY_REQUEST_CODE = 1237

        const val NOTIFICATION_ID = 320750
    }
}