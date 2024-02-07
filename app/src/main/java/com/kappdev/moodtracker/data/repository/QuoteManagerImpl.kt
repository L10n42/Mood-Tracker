package com.kappdev.moodtracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kappdev.moodtracker.domain.converters.LocalDateConverter
import com.kappdev.moodtracker.domain.model.Quote
import com.kappdev.moodtracker.domain.repository.QuoteManager
import com.kappdev.moodtracker.domain.use_case.GetRandomQuote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("quotes_datastore")

class QuoteManagerImpl @Inject constructor(
    private val context: Context,
    private val getRandomQuote: GetRandomQuote
) : QuoteManager {

    companion object {
        private val QuoteId = intPreferencesKey("QUOTE_ID")
        private val Quote = stringPreferencesKey("QUOTE")
        private val QuoteAuthor = stringPreferencesKey("QUOTE_AUTHOR")
        private val LastEditDate = stringPreferencesKey("LAST_EDIT_DATE")
    }

    override suspend fun needUpdate(): Boolean {
        val currentQuote = getQuoteFlow().first()
        val lastEdit = getLastEditDate()
        return currentQuote == null || lastEdit == null || lastEdit < LocalDate.now()
    }

    override suspend fun updateQuote() {
        val quote = getRandomQuote()
        update(quote)
    }

    override suspend fun getQuoteFlow() = context.dataStore.data.map { preferences ->
        val id = preferences[QuoteId]
        val quote = preferences[Quote]
        val author = preferences[QuoteAuthor]
        if (id != null && quote != null && author != null) Quote(id, quote, author) else null
    }

    private suspend fun getLastEditDate(): LocalDate? {
        val dateString = context.dataStore.data.first().get(LastEditDate)
        return dateString?.let(LocalDateConverter::toLocalDate)
    }

    private suspend fun update(quote: Quote) {
        context.dataStore.edit { preferences ->
            preferences[QuoteId] = quote.id
            preferences[Quote] = quote.text
            preferences[QuoteAuthor] = quote.author
            preferences[LastEditDate] = currentDate()
        }
    }

    private fun currentDate(): String {
        return LocalDateConverter.fromLocalDate(LocalDate.now())
    }
}