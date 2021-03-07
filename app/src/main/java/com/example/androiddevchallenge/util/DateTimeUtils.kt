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
package com.example.androiddevchallenge.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

const val DEFAULT_DATE_FORMAT_PATTERN: String = "E MMMM d, yyyy"
const val A_DAY_AS_SECONDS: Long = 86_400 // Seconds in a day

fun Long.minutesToSeconds(): Long = TimeUnit.MINUTES.toSeconds(this)

fun Long.secondsToMillis(): Long = TimeUnit.SECONDS.toMillis(this)

fun Long.millisToSeconds(): Long = TimeUnit.MILLISECONDS.toSeconds(this)

fun Long.millisToMinutes(): Long = TimeUnit.MILLISECONDS.toMinutes(this)

fun Long.minutesToMillis(): Long = TimeUnit.MINUTES.toMillis(this)

fun getFormattedDurationHMS(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)

    return "$hours hours $minutes minutes $seconds seconds"
}

fun getFormattedDate(
    date: Date,
    pattern: String = DEFAULT_DATE_FORMAT_PATTERN,
    locale: Locale = Locale.US
): String = SimpleDateFormat(pattern, locale).format(date)
