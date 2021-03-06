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
package com.example.androiddevchallenge.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timerItemEntity: TimerItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(timerItemEntity: TimerItemEntity)

    @Query("SELECT * FROM timer_item")
    fun observeAll(): Flow<List<TimerItemEntity>>

    @Query("SELECT * FROM timer_item WHERE id = :id")
    fun observeWithId(id: Int): Flow<TimerItemEntity>

    @Delete
    suspend fun delete(timerItemEntity: TimerItemEntity): Int
}
