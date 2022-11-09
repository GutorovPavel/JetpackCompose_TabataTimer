package com.example.tabatatimer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tabatatimer.ui.theme.TabataTimerTheme
import com.example.tabatatimer.ui.theme.add_todo.AddToDoScreen
import com.example.tabatatimer.ui.theme.navigation_drawer.DrawerBody
import com.example.tabatatimer.ui.theme.navigation_drawer.DrawerHeader
import com.example.tabatatimer.ui.theme.navigation_drawer.MenuItem
import com.example.tabatatimer.ui.theme.todo_list.ToDoListScreen
import com.example.tabatatimer.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabataTimerTheme {
                
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "home",
                                    title = "Home",
                                    icon = Icons.Default.Home,
                                    contentDescription = "Go to home page"
                                ),
                                MenuItem(
                                    id = "settings",
                                    title = "Settings",
                                    icon = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                ),
                                MenuItem(
                                    id = "github",
                                    title = "GitHub",
                                    icon = Icons.Default.Info,
                                    contentDescription = "Github"
                                ),
                            ),
                            onItemClick = {

                            }
                        )
                    },
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
                    ) { }
                }

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ) {
                    composable(Routes.TODO_LIST) {
                        ToDoListScreen(onNavigate = {
                                navController.navigate(it.route)
                            }
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
                }
            }
        }
    }
}