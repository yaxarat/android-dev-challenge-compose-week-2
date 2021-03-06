package com.example.androiddevchallenge.di

import androidx.room.Room
import com.example.androiddevchallenge.BaseApplication
import com.example.androiddevchallenge.data.TimerItemDao
import com.example.androiddevchallenge.data.TimerItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideTimerItemDatabase(context: BaseApplication): TimerItemDatabase =
        Room.databaseBuilder(
            context,
            TimerItemDatabase::class.java,
            TimerItemDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesTimerItemDao(timerItemDatabase: TimerItemDatabase): TimerItemDao =
        timerItemDatabase.timerItemDao()
}