package com.kappdev.moodtracker.di

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.kappdev.moodtracker.data.data_source.MoodDatabase
import com.kappdev.moodtracker.data.repository.MoodRepositoryImpl
import com.kappdev.moodtracker.data.repository.QuoteManagerImpl
import com.kappdev.moodtracker.data.repository.ReminderManagerImpl
import com.kappdev.moodtracker.data.repository.SettingsManagerImpl
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.repository.QuoteManager
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.use_case.CopyNote
import com.kappdev.moodtracker.domain.use_case.CopyQuote
import com.kappdev.moodtracker.domain.use_case.GetRandomQuote
import com.kappdev.moodtracker.domain.use_case.RateTheApp
import com.kappdev.moodtracker.domain.use_case.ShareImage
import com.kappdev.moodtracker.domain.use_case.StoreImage
import com.kappdev.moodtracker.domain.util.Toaster
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoodDatabase(app: Application): MoodDatabase {
        return Room.databaseBuilder(app, MoodDatabase::class.java, MoodDatabase.NAME).build()
    }

    @Provides
    @Singleton
    fun provideMoodRepository(db: MoodDatabase): MoodRepository {
        return MoodRepositoryImpl(db.moodDao)
    }

    @Provides
    @Singleton
    fun provideAppSettingsManager(app: Application): SettingsManager {
        return SettingsManagerImpl(app)
    }

    @Provides
    @Singleton
    fun provideStoreImage(app: Application): StoreImage {
        return StoreImage(app)
    }

    @Provides
    @Singleton
    fun provideShareImage(app: Application): ShareImage {
        return ShareImage(app)
    }

    @Provides
    @Singleton
    fun provideRateTheApp(app: Application): RateTheApp {
        return RateTheApp(app)
    }

    @Provides
    @Singleton
    fun provideCopyNote(app: Application, toaster: Toaster): CopyNote {
        return CopyNote(app, toaster)
    }

    @Provides
    @Singleton
    fun provideCopyQuote(app: Application, toaster: Toaster): CopyQuote {
        return CopyQuote(app, toaster)
    }

    @Provides
    @Singleton
    fun provideGetRandomQuote(app: Application): GetRandomQuote {
        return GetRandomQuote(app.resources)
    }

    @Provides
    @Singleton
    fun provideQuoteManage(app: Application, getRandomQuote: GetRandomQuote): QuoteManager {
        return QuoteManagerImpl(app, getRandomQuote)
    }

    @Provides
    @Singleton
    fun provideAppRemainderManager(app: Application): ReminderManager {
        return ReminderManagerImpl(app)
    }

    @Provides
    @Singleton
    fun provideAppNotificationManager(app: Application): NotificationManager {
        return ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
    }
}