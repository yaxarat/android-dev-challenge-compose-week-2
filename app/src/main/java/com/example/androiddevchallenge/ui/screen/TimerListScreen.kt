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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.TimerItemEntity
import com.example.androiddevchallenge.service.CustomTimer
import com.example.androiddevchallenge.ui.component.SwipeableCardComponent
import com.example.androiddevchallenge.ui.component.TimerListBottomSheetContentComponent
import com.example.androiddevchallenge.ui.screen.TimerListIntent.DeleteTimerItemAt
import java.util.HashMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TimerListScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: TimerListScreenViewModel,
) {
    val initialBottomSheetColor = MaterialTheme.colors.surface
    val scope = viewModel.viewModelScope
    val state: MutableState<TimerListState> = viewModel.state
    val timerItemList: List<TimerItemEntity> = state.value.timerItemList
    val itemToTimerMap: HashMap<Int, CustomTimer> = state.value.itemToTimerMap
    val bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    var bottomSheetColor by remember { mutableStateOf(initialBottomSheetColor) }
    val animatedColor by animateColorAsState(targetValue = bottomSheetColor)

    BottomSheetScaffold(
        sheetContent = {
            TimerListBottomSheetContentComponent(
                scope = scope,
                scaffoldState = bottomSheetScaffoldState,
                onCreateTimer = { createTimerItem ->
                    viewModel.onIntention(createTimerItem)
                },
                onChangeColor = { bottomSheetColor = it }
            )
        },
        sheetBackgroundColor = animatedColor,
        sheetElevation = 12.dp,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 64.dp,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
                .padding(top = 24.dp)
        ) {
            itemsIndexed(items = timerItemList) { index, timerItem ->
                itemToTimerMap[timerItem.id]?.let { timer ->
                    SwipeableCardComponent(
                        scope = scope,
                        timerItem = timerItem,
                        countDownTimer = timer,
                        onClickDelete = { viewModel.onIntention(DeleteTimerItemAt(index)) },
                    )
                }
            }
        }
    }
}
