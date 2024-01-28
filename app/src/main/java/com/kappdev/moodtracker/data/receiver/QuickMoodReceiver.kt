package com.kappdev.moodtracker.data.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.model.fromKey
import com.kappdev.moodtracker.domain.use_case.QuickInsertMood
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuickMoodReceiver : BroadcastReceiver() {

    companion object {
        const val QUICK_MOOD_ACTION = "QUICK_MOOD"
        const val MOOD_TYPE_EXTRA = "MOOD_TYPE"
    }

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var quickInsertMood: QuickInsertMood

    override fun onReceive(context: Context, intent: Intent) {
        val moodKey = intent.getStringExtra(MOOD_TYPE_EXTRA)

        if (intent.action == QUICK_MOOD_ACTION && moodKey != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val mood = MoodType.fromKey(moodKey)
                quickInsertMood(mood)
                notificationManager.cancel(ReminderReceiver.NOTIFICATION_ID)
            }
        }
    }

}