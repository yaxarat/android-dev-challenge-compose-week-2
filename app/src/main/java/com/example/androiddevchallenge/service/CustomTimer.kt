package com.example.androiddevchallenge.service

import kotlinx.coroutines.flow.Flow

interface CustomTimer {
    fun start(
        countUpToSeconds: Long,
        withSecondsInterval: Long,
        inCountDownMode: Boolean
    )

    fun stop()

    fun cancel()

    val millisecondsRemaining: Flow<Long>

    val millisecondsElapsed: Flow<Long>

    fun getElapsedSessionLength(): Long
}