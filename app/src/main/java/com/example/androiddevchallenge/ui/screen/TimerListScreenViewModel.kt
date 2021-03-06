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
import com.example.androiddevchallenge.ui.screen.TimerListIntent.CreateNewTimer
import com.example.androiddevchallenge.ui.screen.TimerListIntent.UpdateTimerList
import com.example.androiddevchallenge.util.minutesToSeconds
import com.github.ajalt.timberkt.d
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerListScreenViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val initialState = TimerListState(timerItemList = emptyList())

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
                d { "current state: $state\n intent: $intent" }
            }

            when(intent) {
                is UpdateTimerList -> {
                    state.value = currentState.copy(timerItemList = intent.newList)
                }
                is CreateNewTimer -> {
                    createNewTimerItem(
                        title = intent.name,
                        color = intent.color,
                        timerInSeconds = getTimeInSecondsLong(timeInMinutesString = intent.timeInMinutes)
                    )
                }
            }
        }
    }

    private fun getTimeInSecondsLong(timeInMinutesString: String): Long {
        val minutesOrZero = timeInMinutesString.toLongOrNull() ?: 0L

        return minutesOrZero.minutesToSeconds()
    }

    // TODO: convert to upsert usecase?
    private fun createNewTimerItem(
        title: String,
        color: String,
        timerInSeconds: Long
    ) {
        viewModelScope.launch {
            repository.insertTimerItem(
                TimerItemEntity(
                    id = 0,
                    title = title,
                    color = color,
                    timerInSeconds = timerInSeconds
                )
            )
        }
    }

    companion object {
        val viewModelKey: String = TimerListScreenViewModel::class.java.simpleName
    }
}

data class TimerListState(val timerItemList: List<TimerItemEntity>)

sealed class TimerListIntent {
    data class UpdateTimerList(val newList: List<TimerItemEntity>) : TimerListIntent()
    data class CreateNewTimer(val name: String, val timeInMinutes: String, val color: String) : TimerListIntent()
}
