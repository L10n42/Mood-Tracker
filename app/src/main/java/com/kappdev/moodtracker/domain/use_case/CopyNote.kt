package com.kappdev.moodtracker.domain.use_case

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.util.Toaster
import javax.inject.Inject

class CopyNote @Inject constructor(
    private val context: Context,
    private val toaster: Toaster
) {

    operator fun invoke(note: String) {
        if (note.isEmpty()) {
            toaster.show(R.string.empty_note)
        } else {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Note", note)
            clipboardManager.setPrimaryClip(clip)
            toaster.show(R.string.copied)
        }
    }
}