package com.kappdev.moodtracker.domain.use_case

import android.content.res.Resources
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Quote
import javax.inject.Inject

class GetRandomQuote @Inject constructor(
    private val resources: Resources
) {

    operator fun invoke(): Quote {
        val quotesArray = resources.getStringArray(R.array.quotes)
        val quotes = quotesArray.mapIndexed(::parseQuote)
        return quotes.random()
    }

    private fun parseQuote(id: Int, rawQuote: String): Quote {
        val parts = rawQuote.split("-")
        val text = parts[0].trim()
        val author = parts.getOrNull(1)?.trim() ?: resources.getString(R.string.unknown_author)
        return Quote(id, text, author)
    }
}