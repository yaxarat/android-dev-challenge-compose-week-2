package com.example.androiddevchallenge.di

import com.example.androiddevchallenge.data.Repository
import com.example.androiddevchallenge.data.RepositoryImpl
import com.example.androiddevchallenge.data.TimerItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(timerItemDao: TimerItemDao): Repository = RepositoryImpl(timerItemDao)
}