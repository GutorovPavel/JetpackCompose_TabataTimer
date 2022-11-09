package com.example.tabatatimer.data

import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    suspend fun insertToDo(todo: ToDo)

    suspend fun deleteToDo(todo: ToDo)

    suspend fun getToDoById(id: Int): ToDo?

    fun getToDos(): Flow<List<ToDo>>
}