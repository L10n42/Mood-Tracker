package com.kappdev.moodtracker.di

import android.app.Application
import androidx.room.Room
import com.kappdev.moodtracker.data.data_source.MoodDatabase
import com.kappdev.moodtracker.data.repository.MoodRepositoryImpl
import com.kappdev.moodtracker.data.repository.SettingsManagerImpl
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.use_case.StoreImage
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
    fun provideSettingsManager(app: Application): SettingsManager {
        return SettingsManagerImpl(app)
    }

    @Provides
    @Singleton
    fun provideStoreImage(app: Application): StoreImage {
        return StoreImage(app)
    }
}