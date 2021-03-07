package com.example.androiddevchallenge.service

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf

class Timer(limitMillis: Long): CountDownTimer(limitMillis, 1000) {
    val millisUntilFinished = mutableStateOf(limitMillis)

    override fun onTick(millisUntilFinished: Long) {
        this.millisUntilFinished.value = millisUntilFinished
    }

    override fun onFinish() {

    }
}