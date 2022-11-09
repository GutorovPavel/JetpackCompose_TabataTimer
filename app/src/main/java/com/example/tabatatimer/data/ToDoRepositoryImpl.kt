package com.example.tabatatimer.data

import kotlinx.coroutines.flow.Flow

class ToDoRepositoryImpl(
    private val dao: ToDoDao
): ToDoRepository {
    override suspend fun insertToDo(todo: ToDo) {
        dao.insertToDo(todo)
    }

    override suspend fun deleteToDo(todo: ToDo) {
        dao.deleteToDo(todo)
    }

    override suspend fun getToDoById(id: Int): ToDo? {
        return dao.getToDoById(id)
    }

    override fun getToDos(): Flow<List<ToDo>> {
        return dao.getToDos()
    }
}