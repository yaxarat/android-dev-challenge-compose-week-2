/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
