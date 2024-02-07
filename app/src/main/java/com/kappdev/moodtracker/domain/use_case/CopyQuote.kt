package com.kappdev.moodtracker.domain.use_case

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Quote
import com.kappdev.moodtracker.domain.util.Toaster
import javax.inject.Inject

class CopyQuote @Inject constructor(
    private val context: Context,
    private val toaster: Toaster
) {

    operator fun invoke(quote: Quote) {
        val styledQuote = styleQuote(quote)
        copyToClipboard(styledQuote)
        toaster.show(R.string.copied)
    }

    private fun styleQuote(quote: Quote) = buildString {
        append("\"")
        append(quote.text)
        append("\"")
        append(" - ")
        append(quote.author)
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Quote", text)
        clipboardManager.setPrimaryClip(clip)
    }
}