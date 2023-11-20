package com.kappdev.moodtracker.domain.util

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Toaster @Inject constructor(
    private val application: Application
) {
    suspend fun showSuspended(message: String, duration: Int = Toast.LENGTH_SHORT) {
        withContext(Dispatchers.Main) {
            show(message, duration)
        }
    }

    suspend fun showSuspended(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
        withContext(Dispatchers.Main) {
            show(messageRes, duration)
        }
    }

    fun show(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(application, message, duration).show()
    }

    fun show(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(application, application.getString(messageRes), duration).show()
    }
}