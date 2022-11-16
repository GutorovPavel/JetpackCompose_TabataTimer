package com.example.tabatatimer.ui.theme.todo_list

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tabatatimer.Timer
import com.example.tabatatimer.service.ServiceHelper
import com.example.tabatatimer.service.StopwatchService
import com.example.tabatatimer.service.StopwatchState
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_CANCEL
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_START
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_STOP
import com.example.tabatatimer.util.UiEvent
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ToDoListScreen(
    stopwatchService: StopwatchService,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ToDoListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var currentTime by stopwatchService.currentTime
    var currentState by stopwatchService.currentState

    val todos = viewModel.todos.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {

                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(ToDoListEvent.OnAddToDoClick)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Timer(stopwatchService, modifier = Modifier.size(250.dp)) // Timer
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Column {
//                    Text(text = currentTime.toString())
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Row {
//                        Button(onClick = {
//                            ServiceHelper.triggerForegroundService(
//                                context = context,
//                                action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
//                                else ACTION_SERVICE_START
//                            )
//                        }) {
//                            Text(text = "Start")
//                        }
//                        Button(onClick = {
//                            ServiceHelper.triggerForegroundService(
//                                context = context, action = ACTION_SERVICE_CANCEL
//                            )
//                        }) {
//                            Text(text = "Cancel")
//
//                        }
//                    }
//                }
//            }
            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .height(193.dp)
            ) {
                items(todos.value) { todo ->
                    ToDoItem(
                        todo = todo,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(ToDoListEvent.OnToDoClick(todo))
                            }
                            .padding(10.dp)
                    )
                }
            }
            //Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "                                  Add new task",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
