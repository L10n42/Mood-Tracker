package com.kappdev.moodtracker.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    lateinit var settings: SettingsManager

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null && action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val remainder = settings.getValueFlow(Settings.Reminder).first()
                if (remainder.enabled) {
                    reminderManager.setRemainder(remainder.time)
                }
            }
        }
    }
}