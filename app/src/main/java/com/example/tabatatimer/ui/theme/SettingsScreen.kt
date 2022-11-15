package com.example.tabatatimer.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tabatatimer.util.SettingsBundle

@Composable
fun SettingsScreen(
    settings: SettingsBundle,
    onSettingsChanged: (SettingsBundle) -> Unit
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.padding(30.dp)
        ) {
            Text( text = "Dark Mode", fontSize = 22.sp, modifier = Modifier.weight(1f))
            Switch(checked = settings.isDarkMode, onCheckedChange = {
                    onSettingsChanged.invoke(settings.copy(isDarkMode = !settings.isDarkMode))
                },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}