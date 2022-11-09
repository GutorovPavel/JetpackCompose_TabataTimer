package com.example.tabatatimer.ui.theme.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.data.ToDo
import com.example.tabatatimer.data.ToDoRepository
import com.example.tabatatimer.util.Routes
import com.example.tabatatimer.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: ToDoRepository
): ViewModel() {

    val todos = repository.getToDos()

    private val _uiEvent = Channel<UiEvent>() //??
    val uiEvent = _uiEvent.receiveAsFlow() //??

    private var deletedToDo: ToDo? = null

    fun onEvent(event: ToDoListEvent) {
        when (event) {
            is ToDoListEvent.OnAddToDoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is ToDoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertToDo(event.todo.copy(isDone = event.isDone))
                }
            }
            is ToDoListEvent.DeleteToDo -> {
                viewModelScope.launch {
                    deletedToDo = event.todo
                    repository.deleteToDo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "ToDo deleted",
                        action = "Undo"
                    ))
                }
            }
            is ToDoListEvent.OnUndoDelete -> {
                deletedToDo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertToDo(todo)
                    }
                }
            }
            is ToDoListEvent.OnToDoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId = ${event.todo.id}"))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}