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