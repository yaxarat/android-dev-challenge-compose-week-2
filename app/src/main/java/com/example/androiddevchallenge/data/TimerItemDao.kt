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

    @Query("SELECT * FROM subject")
    fun observeAll(): Flow<List<TimerItemEntity>>

    @Query("SELECT * FROM subject WHERE id = :id")
    fun observeWithId(id: Int): Flow<TimerItemEntity>

    @Delete
    suspend fun delete(timerItemEntity: TimerItemEntity) : Int
}