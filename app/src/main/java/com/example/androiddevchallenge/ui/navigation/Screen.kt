package com.example.androiddevchallenge.ui.navigation

sealed class Screen(val route: String) {
    object TimerList: Screen(route = "timerList")
}
