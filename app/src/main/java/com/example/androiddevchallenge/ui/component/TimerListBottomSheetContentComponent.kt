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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TimerListBottomSheetContentComponent(
    scaffoldState: BottomSheetScaffoldState,
    onCreateTimer: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val title = remember { mutableStateOf("") }
    val timerLength = remember { mutableStateOf("") }
    // val timerColor = remember { mutableStateOf("") }

    BottomSheetTitleComponent(
        expandedTitle = "Swipe down to cancel",
        collapsedTitle = "Swipe up to create new timer",
        bottomSheetScaffoldState = scaffoldState
    )


    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Name of the activity", style = MaterialTheme.typography.body1) },
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
            value = timerLength.value,
            onValueChange = { timerLength.value = it },
            label = { Text("Initial hours to start", style = MaterialTheme.typography.body1) },
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

        Text(
            text = "Card Color",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    top = 16.dp,
                    end = 32.dp
                )
        )


        // Update or use library
//        ScrollableRow(
//            horizontalArrangement = Arrangement.SpaceAround,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
//            scrollState = scrollState
//        ) {
//            for (color in CardColors.values()) {
//                ColorPickerTile(color = color, onClick = { onColorChange(it) })
//            }
//        }

        Button(
            onClick = {
//                onCreateSubject()
//                LaunchedEffect(Unit) {
//                    scaffoldState.bottomSheetState.collapse()
//                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Save", style = MaterialTheme.typography.body1)
        }
    }
}
