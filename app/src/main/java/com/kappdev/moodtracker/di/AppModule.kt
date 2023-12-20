package com.kappdev.moodtracker.di

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.kappdev.moodtracker.data.data_source.MoodDatabase
import com.kappdev.moodtracker.data.repository.MoodRepositoryImpl
import com.kappdev.moodtracker.data.repository.ReminderManagerImpl
import com.kappdev.moodtracker.data.repository.SettingsManagerImpl
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.repository.ReminderManager
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.use_case.ShareImage
import com.kappdev.moodtracker.domain.use_case.StoreImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoodDatabase(app: Application): MoodDatabase {
        return Room.databaseBuilder(app, MoodDatabase::class.java, MoodDatabase.NAME)
            .fallbackToDestructiveMigration()
            .build()
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
    fun provideAppRemainderManager(app: Application): ReminderManager {
        return ReminderManagerImpl(app)
    }

    @Provides
    @Singleton
    fun provideAppNotificationManager(app: Application): NotificationManager {
        return ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
    }
}