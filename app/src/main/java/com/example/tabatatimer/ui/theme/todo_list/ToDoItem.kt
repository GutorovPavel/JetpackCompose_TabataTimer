package com.example.tabatatimer.ui.theme.todo_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tabatatimer.data.ToDo

@Composable
fun ToDoItem (
    todo: ToDo,
    onEvent: (ToDoListEvent) -> Unit,    // lambda func ???
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text (
                text = todo.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            todo.description?.let {
                Text(text = it)
            }
        }
        Checkbox(checked = todo.isDone, onCheckedChange = {
                isChecked -> onEvent(ToDoListEvent.OnDoneChange(todo, isChecked))
        })
        IconButton(onClick = {
            onEvent(ToDoListEvent.DeleteToDo(todo))
        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}