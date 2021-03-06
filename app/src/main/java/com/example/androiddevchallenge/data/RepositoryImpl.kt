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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class RepositoryImpl(private val timerItemDao: TimerItemDao) : Repository {

    private val timerItemChannel = Channel<TimerItemEntity>(capacity = UNLIMITED)

    private val timerItemListChannel = Channel<List<TimerItemEntity>>(capacity = UNLIMITED)

    override val timerItem: Flow<TimerItemEntity> = timerItemChannel.receiveAsFlow()

    override val timerItemList: Flow<List<TimerItemEntity>> = timerItemListChannel.receiveAsFlow()

    override suspend fun insertTimerItem(timerItemEntity: TimerItemEntity) {
        timerItemDao.insert(timerItemEntity)
    }

    override suspend fun updateTimerItem(timerItemEntity: TimerItemEntity) {
        timerItemDao.update(timerItemEntity)
    }

    override suspend fun deleteTimerItem(timerItemEntity: TimerItemEntity) {
        timerItemDao.delete(timerItemEntity)
    }

    override fun observeTimerItemWithId(id: Int, scope: CoroutineScope) {
        timerItemDao.observeWithId(id)
            .filterNotNull()
            .onEach {
                timerItemChannel.offer(it)
            }
            .launchIn(scope)
    }

    override fun getAllTimerItem(scope: CoroutineScope) {
        timerItemDao.observeAll()
            .onEach {
                timerItemListChannel.offer(it)
            }
            .launchIn(scope)
    }
}
