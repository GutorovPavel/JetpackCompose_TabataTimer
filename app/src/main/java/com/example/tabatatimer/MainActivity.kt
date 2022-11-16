package com.example.tabatatimer

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tabatatimer.service.StopwatchService
import com.example.tabatatimer.ui.theme.SettingsScreen
import com.example.tabatatimer.ui.theme.TabataTimerTheme
import com.example.tabatatimer.ui.theme.add_todo.AddToDoScreen
import com.example.tabatatimer.ui.theme.navigation_drawer.DrawContent
import com.example.tabatatimer.ui.theme.todo_list.ToDoListScreen
import com.example.tabatatimer.util.Routes
import com.example.tabatatimer.util.SettingsBundle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkModeValue = false
            val isDarkMode = remember { mutableStateOf(isDarkModeValue) }

            TabataTimerTheme(
                darkTheme = isDarkMode.value
            ) {

                if (isBound) {

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            DrawContent(navController = navController, drawerState = drawerState)
                        },
                        scrimColor = Color(0xE4000000)
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(text = "Timers") },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                if (drawerState.isClosed) {
                                                    scope.launch { drawerState.open() }
                                                } else {
                                                    scope.launch { drawerState.close() }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Routes.TODO_LIST
                            ) {
                                composable(Routes.TODO_LIST) {
                                    ToDoListScreen(onNavigate = {
                                            navController.navigate(it.route)
                                        },
                                        stopwatchService = stopwatchService
                                    )
                                }
                                composable(
                                    Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                                    arguments = listOf(
                                        navArgument(name = "todoId") {
                                            type = NavType.IntType
                                            defaultValue = -1
                                        }
                                    )
                                ) {
                                    AddToDoScreen(onPopBackStack = {
                                        navController.popBackStack()
                                    })
                                }
                                composable(Routes.SETTINGS) {
                                    val settings = SettingsBundle(
                                        isDarkMode = isDarkMode.value
                                    )

                                    SettingsScreen(
                                        settings = settings,
                                        onSettingsChanged = {
                                            isDarkMode.value = it.isDarkMode
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}