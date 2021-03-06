package com.example.androiddevchallenge.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TimerItemEntity::class], version = 1)
abstract class TimerItemDatabase : RoomDatabase() {

    abstract fun timerItemDao(): TimerItemDao

    companion object {
        const val DATABASE_NAME = "timer_item_dao"
    }
}