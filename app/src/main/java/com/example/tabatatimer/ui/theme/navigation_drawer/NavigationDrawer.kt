package com.example.tabatatimer.ui.theme.navigation_drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun DrawerHeader() {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Timers", fontSize = 50.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DrawerFooter() {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "powered by @visshiisort // " +
                "2022", fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Thin)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawContent(
    navController: NavHostController,
    drawerState: DrawerState
) {
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .padding(30.dp)
            .width(350.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawerHeader()
        Spacer(modifier = Modifier.height(370.dp))
        DrawerItems(navController = navController, drawerState = drawerState)
        Spacer(modifier = Modifier.height(80.dp))
        DrawerFooter()
    }
}