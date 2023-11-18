package com.kappdev.moodtracker.di

import android.app.Application
import androidx.room.Room
import com.kappdev.moodtracker.data.data_source.MoodDatabase
import com.kappdev.moodtracker.data.repository.MoodRepositoryImpl
import com.kappdev.moodtracker.domain.repository.MoodRepository
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


}