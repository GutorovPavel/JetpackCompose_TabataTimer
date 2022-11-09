package com.example.tabatatimer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // if ID is exist -> replace
    suspend fun insertToDo(todo: ToDo)

    // what is "suspend"?

    @Delete
    suspend fun deleteToDo(todo: ToDo)

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getToDoById(id: Int): ToDo?

    @Query("SELECT * FROM todo")
    fun getToDos(): Flow<List<ToDo>>  // Flow???
}