package com.example.tabatatimer.ui.theme.todo_list

import com.example.tabatatimer.data.ToDo

sealed class ToDoListEvent {

    data class DeleteToDo(val todo: ToDo): ToDoListEvent()
    data class OnDoneChange(val todo: ToDo, val isDone: Boolean): ToDoListEvent()
    object OnUndoDelete: ToDoListEvent()
    data class OnToDoClick(val todo: ToDo) : ToDoListEvent()
    object OnAddToDoClick: ToDoListEvent()
}
