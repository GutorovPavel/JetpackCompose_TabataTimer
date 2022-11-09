package com.example.tabatatimer.ui.theme.add_todo

sealed class AddToDoEvent {

    data class OnTitleChange(val title: String): AddToDoEvent()
    data class OnDescriptionChange(val description: String) : AddToDoEvent()
    object OnSaveToDoClick: AddToDoEvent()
}
