package com.example.androiddevchallenge.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface Repository {

    val timerItem: Flow<TimerItemEntity>

    val timerItemList: Flow<List<TimerItemEntity>>

    fun observeTimerItemWithId(id: Int, scope: CoroutineScope)

    fun getAllTimerItem(scope: CoroutineScope)

    suspend fun insertTimerItem(timerItemEntity: TimerItemEntity)

    suspend fun updateTimerItem(timerItemEntity: TimerItemEntity)

    suspend fun deleteTimerItem(timerItemEntity: TimerItemEntity)
}