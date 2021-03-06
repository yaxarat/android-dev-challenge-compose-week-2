package com.example.androiddevchallenge.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TimerListScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: TimerListScreenViewModel,
) {
    Text("We are the screen!")
}