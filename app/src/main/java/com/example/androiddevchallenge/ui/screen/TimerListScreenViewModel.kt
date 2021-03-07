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
package com.example.androiddevchallenge.ui.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Repository
import com.example.androiddevchallenge.data.TimerItemEntity
import com.example.androiddevchallenge.service.Timer
import com.example.androiddevchallenge.ui.screen.TimerListIntent.CreateNewTimer
import com.example.androiddevchallenge.ui.screen.TimerListIntent.UpdateTimerList
import com.example.androiddevchallenge.util.exhaustive
import com.example.androiddevchallenge.util.minutesToSeconds
import com.example.androiddevchallenge.util.secondsToMillis
import com.github.ajalt.timberkt.d
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerListScreenViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val initialState = TimerListState(
        timerItemList = emptyList(),
        itemToTimerMap = hashMapOf()
    )

    val state: MutableState<TimerListState> = mutableStateOf(initialState)

    init {
        repository.timerItemList
            .distinctUntilChanged()
            .onEach { newList ->
                onIntention(UpdateTimerList(newList))
            }
            .launchIn(viewModelScope)

        repository.getAllTimerItem(viewModelScope)
    }

    fun onIntention(intent: TimerListIntent) {
        viewModelScope.launch {
            val currentState = state.value.also { state ->
                logStateAndIntent(state = state, intent = intent)
            }

            when(intent) {
                is UpdateTimerList -> {
                    state.value = currentState.copy(
                        timerItemList = intent.newList,
                        itemToTimerMap = assignTimerIfMissing(
                            timerItemList = intent.newList,
                            itemToTimerMap = currentState.itemToTimerMap
                        )
                    )
                }
                is CreateNewTimer -> {
                    createNewTimerItem(
                        title = intent.name,
                        color = intent.color,
                        timerInSeconds = getTimeInSecondsLong(intent.timeInMinutes)
                    )
                }
                is TimerListIntent.DeleteTimerItemAt -> {
                    val timerItemToDelete = currentState.timerItemList[intent.index]
                    deleteTimerItem(timerItemToDelete)
                }
            }.exhaustive
        }
    }

    private suspend fun deleteTimerItem(timerItem: TimerItemEntity) =
        repository.deleteTimerItem(timerItem)

    private suspend fun createNewTimerItem(
        title: String,
        color: String,
        timerInSeconds: Long
    ) =
        repository.insertTimerItem(
            TimerItemEntity(
                id = 0,
                title = title,
                color = color,
                timerInSeconds = timerInSeconds
            )
        )

    private fun getTimeInSecondsLong(timeInMinutesString: String): Long {
        val minutesOrZero = timeInMinutesString.toLongOrNull() ?: 0L

        return minutesOrZero.minutesToSeconds()
    }

    private fun assignTimerIfMissing(
        timerItemList: List<TimerItemEntity>,
        itemToTimerMap: HashMap<Int, Timer>
    ): HashMap<Int, Timer> {
        timerItemList
            .filterNot { itemToTimerMap.containsKey(it.id) }
            .forEach { timerItem ->
                itemToTimerMap[timerItem.id] = getCountDownTimerWithLimit(timerItem.timerInSeconds.secondsToMillis())
            }

        return itemToTimerMap
    }

    private fun getCountDownTimerWithLimit(limitMillis: Long): Timer =
        Timer(limitMillis)

    private fun logStateAndIntent(
        state: TimerListState,
        intent: TimerListIntent
    ) =
        d { "current state: $state\n intent: $intent" }

    companion object {
        val viewModelKey: String = TimerListScreenViewModel::class.java.simpleName
    }
}

data class TimerListState(
    val timerItemList: List<TimerItemEntity>,
    val itemToTimerMap: HashMap<Int, Timer>,
)

sealed class TimerListIntent {
    data class UpdateTimerList(val newList: List<TimerItemEntity>) : TimerListIntent()
    data class CreateNewTimer(
        val name: String,
        val timeInMinutes: String,
        val color: String
    ) : TimerListIntent()
    data class DeleteTimerItemAt(val index: Int) : TimerListIntent()
}
