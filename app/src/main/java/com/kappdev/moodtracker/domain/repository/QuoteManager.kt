package com.kappdev.moodtracker.domain.repository

import com.kappdev.moodtracker.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteManager {

    suspend fun needUpdate(): Boolean

    suspend fun updateQuote()

    suspend fun getQuoteFlow(): Flow<Quote?>

}