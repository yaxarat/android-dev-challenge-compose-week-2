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
package com.example.androiddevchallenge.service

import android.os.CountDownTimer
import com.example.androiddevchallenge.util.millisToSeconds
import com.example.androiddevchallenge.util.secondsToMillis
import com.github.ajalt.timberkt.Timber.d
import kotlinx.coroutines.flow.MutableStateFlow

class CustomTimerImpl : CustomTimer {
    override val millisecondsRemaining: MutableStateFlow<Long> = MutableStateFlow(0L)

    override val millisecondsElapsed: MutableStateFlow<Long> = MutableStateFlow(0L)

    private var sessionInMillis: Long = 0L

    private var remainingSessionInMillis: Long = 0L

    private var timer: CountDownTimer? = null

    override fun start(
        countUpToSeconds: Long,
        withSecondsInterval: Long,
        inCountDownMode: Boolean
    ) {
        val counterCeiling: Long = if (remainingSessionInMillis == 0L) {
            d { "Timer started with second limit: $countUpToSeconds interval: $withSecondsInterval in countdown mode: $inCountDownMode" }
            countUpToSeconds.secondsToMillis()
        } else {
            d { "Timer resumed with remaining session: ${ remainingSessionInMillis.millisToSeconds() } interval: $withSecondsInterval in countdown mode: $inCountDownMode" }
            remainingSessionInMillis
        }

        timer = object : CountDownTimer(counterCeiling, withSecondsInterval.secondsToMillis()) {
            override fun onTick(millisUntilFinished: Long) {
                if (inCountDownMode) {
                    millisUntilFinished.let {
                        millisecondsRemaining.value = it
                        remainingSessionInMillis = it
                    }
                    sessionInMillis += 1L.secondsToMillis()
                } else {
                    sessionInMillis += 1L.secondsToMillis()
                    millisecondsElapsed.value = sessionInMillis
                }
            }

            override fun onFinish() { /* Notify alarm receiver */ }
        }

        timer?.start()
    }

    override fun stop() {
        timer?.cancel()

        d { "Timer stopped" }
    }

    override fun cancel() {
        timer?.cancel()
        sessionInMillis = 0L
        remainingSessionInMillis = 0L

        d { "Timer cancelled" }
    }

    override fun getElapsedSessionLength(): Long {
        d { "Elapsed session fetched: ${ sessionInMillis.millisToSeconds() }" }

        return sessionInMillis
    }
}
