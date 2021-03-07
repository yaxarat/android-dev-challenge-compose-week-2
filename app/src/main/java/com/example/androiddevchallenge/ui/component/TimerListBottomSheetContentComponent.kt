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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.resource.CardColors
import com.example.androiddevchallenge.ui.screen.TimerListIntent
import com.example.androiddevchallenge.ui.screen.TimerListIntent.CreateNewTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TimerListBottomSheetContentComponent(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    onCreateTimer: (TimerListIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    var name by remember { mutableStateOf("") }
    var timerLength by remember { mutableStateOf("") }
    var timerColor by remember { mutableStateOf("") }

    BottomSheetTitleComponent(
        expandedTitle = "Swipe down to cancel",
        collapsedTitle = "Swipe up to create a new timer",
        bottomSheetScaffoldState = scaffoldState
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            state = lazyListState
        ) {
            items(CardColors.values()) {
                ColorPickerTileComponent(
                    color = it,
                    onClick = { color ->
                        timerColor = color
                    }
                )
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name of the timer", style = MaterialTheme.typography.body1) },
            placeholder = { Text("Practice chess", style = MaterialTheme.typography.body1) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hideSoftwareKeyboard()
                }
            )
        )

        OutlinedTextField(
            value = timerLength,
            onValueChange = { timerLength = it },
            label = { Text("Count down time in minutes", style = MaterialTheme.typography.body1) },
            placeholder = { Text("30", style = MaterialTheme.typography.body1) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hideSoftwareKeyboard()
                }
            )
        )

        Button(
            onClick = {
                onCreateTimer(
                    CreateNewTimer(
                        name = name,
                        timeInMinutes = timerLength,
                        color = timerColor
                    )
                )

                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                    keyboardController?.hideSoftwareKeyboard()
                }

                name = ""
                timerLength = ""
            },
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Save", style = MaterialTheme.typography.body1)
        }
    }
}
