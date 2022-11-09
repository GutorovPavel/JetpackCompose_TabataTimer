package com.example.tabatatimer.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDo::class],
    version = 1
)
abstract class ToDoDatabase: RoomDatabase() {
    abstract val dao: ToDoDao
}