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
package com.example.androiddevchallenge.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.data.TimerItemEntity
import com.example.androiddevchallenge.service.CustomTimer
import com.example.androiddevchallenge.ui.component.TimerState.CANCELLED
import com.example.androiddevchallenge.ui.component.TimerState.PAUSED
import com.example.androiddevchallenge.ui.component.TimerState.STARTED
import com.example.androiddevchallenge.ui.resource.CardColors
import com.example.androiddevchallenge.util.exhaustive
import com.example.androiddevchallenge.util.getFormattedDurationHMS
import com.example.androiddevchallenge.util.secondsToMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SwipeableCardComponent(
    scope: CoroutineScope,
    timerItem: TimerItemEntity,
    countDownTimer: CustomTimer,
    onClickDelete: () -> Unit,
) {
    val swipeableState = rememberSwipeableState(CardState.DEFAULT)
    val anchors: Map<Float, CardState> = mapOf(
        -200f to CardState.DELETE_LEFT,
        0f to CardState.DEFAULT,
        200f to CardState.DELETE_RIGHT
    )
    val frozenAnchor: Map<Float, CardState> = mapOf(0f to CardState.DEFAULT)
    var timerState by remember { mutableStateOf(CANCELLED) }
    var expanded by remember { mutableStateOf(false) }
    var currentTimerValue by remember { mutableStateOf(getFormattedDurationHMS(timerItem.timerInSeconds.secondsToMillis())) }
    var bottomCardHeight by remember { mutableStateOf(0.dp) }

    BoxWithConstraints(modifier = Modifier.animateContentSize()) {

        if (expanded) {
            BottomSwipeableCardComponent(
                onClickDelete = onClickDelete,
                height = bottomCardHeight
            )
        }

        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 12.dp,
            backgroundColor = CardColors.valueOf(timerItem.color).color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 16.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    bottomCardHeight = layoutCoordinates.size.height.dp
                }
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swipeableState,
                    anchors = if (expanded) anchors else frozenAnchor,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            expanded = !expanded
                        }
                    )
            ) {
                Text(
                    text = timerItem.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(start = 16.dp, top = 16.dp, end = 32.dp, bottom = 32.dp)
                )

                if (expanded) {
                    Text(
                        text = currentTimerValue,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                    )

                    Crossfade(targetState = timerState) {
                        when (it) {
                            STARTED -> {
                                countDownTimer.millisecondsRemaining
                                    .onEach {
                                        currentTimerValue = getFormattedDurationHMS(it)
                                    }
                                    .launchIn(scope)

                                OutlinedButton(
                                    onClick = {
                                        timerState = PAUSED
                                        countDownTimer.stop()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                                ) {
                                    Text(text = "PAUSE")
                                }
                            }
                            PAUSED -> {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            timerState = STARTED
                                            countDownTimer.start(
                                                countUpToSeconds = timerItem.timerInSeconds,
                                                withSecondsInterval = 1,
                                                inCountDownMode = true
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .padding(start = 16.dp, end = 9.dp, bottom = 32.dp)
                                    ) {
                                        Text(text = "RESUME")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            timerState = CANCELLED
                                            countDownTimer.cancel()
                                            currentTimerValue = getFormattedDurationHMS(timerItem.timerInSeconds.secondsToMillis())
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 9.dp, end = 16.dp, bottom = 32.dp)
                                    ) {
                                        Text(text = "CANCEL")
                                    }
                                }
                            }
                            CANCELLED -> {
                                OutlinedButton(
                                    onClick = {
                                        timerState = STARTED
                                        countDownTimer.start(
                                            countUpToSeconds = timerItem.timerInSeconds,
                                            withSecondsInterval = 1,
                                            inCountDownMode = true
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                                ) {
                                    Text(text = "START")
                                }
                            }
                        }.exhaustive
                    }
                }
            }
        }
    }
}

enum class CardState {
    DELETE_LEFT,
    DELETE_RIGHT,
    DEFAULT,
}

enum class TimerState {
    STARTED,
    PAUSED,
    CANCELLED
}
