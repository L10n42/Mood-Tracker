package com.kappdev.moodtracker.domain.use_case

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class RateTheApp @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val GOOGLE_PLAY_LINK = "market://details?id="
        private const val BROWSER_LINK = "https://play.google.com/store/apps/details?id="
    }

    fun launch() {
        try {
            val intent = Intent(
                /* action = */ Intent.ACTION_VIEW,
                /* uri = */ Uri.parse(GOOGLE_PLAY_LINK + context.packageName)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // If Google Play Store is not installed, open the app page in a browser
            val intent = Intent(
                /* action = */ Intent.ACTION_VIEW,
                /* uri = */ Uri.parse(BROWSER_LINK + context.packageName)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}