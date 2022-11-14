package com.example.tabatatimer.ui.theme.navigation_drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.tabatatimer.util.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItems(navController: NavController, drawerState: DrawerState) {

    var scope = rememberCoroutineScope()
    var currentBackStackEntryAsState = navController.currentBackStackEntryAsState()
    var destination = currentBackStackEntryAsState.value?.destination

    NavigationDrawerItem(
        icon = { Icon (imageVector = Icons.Filled.Home, contentDescription = "TIMER") },
        label = { Text (text = "TIMER") },
        selected = destination?.route == Routes.TODO_LIST,
        onClick = {
            navController.navigate(Routes.TODO_LIST, navOptions {
                this.launchSingleTop = true
                this.restoreState = true
            })
            scope.launch { drawerState.close() }
        },
        modifier = Modifier.padding(10.dp)
    )
    NavigationDrawerItem(
        icon = { Icon (imageVector = Icons.Filled.Settings, contentDescription = "Settings") },
        label = { Text (text = "Settings") },
        selected = destination?.route == Routes.SETTINGS,
        onClick = {
            navController.navigate(Routes.SETTINGS, navOptions {
                this.launchSingleTop = true
                this.restoreState = true
            })
            scope.launch { drawerState.close() }
        },
        modifier = Modifier.padding(10.dp)
    )
}