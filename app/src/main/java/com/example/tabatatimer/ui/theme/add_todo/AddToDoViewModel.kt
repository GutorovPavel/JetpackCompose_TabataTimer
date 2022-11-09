package com.example.tabatatimer.ui.theme.add_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.data.ToDo
import com.example.tabatatimer.data.ToDoRepository
import com.example.tabatatimer.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToDoViewModel @Inject constructor(
    private val repository: ToDoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<ToDo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>() //??
    val uiEvent = _uiEvent.receiveAsFlow() //??

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getToDoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description
                    this@AddToDoViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: AddToDoEvent) {
        when (event) {
            is AddToDoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddToDoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddToDoEvent.OnSaveToDoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "The title can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertToDo(
                        ToDo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id
                        )
                    )
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}