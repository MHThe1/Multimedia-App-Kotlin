package com.classwork.multimediaapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun BroadCastScreen(navController: NavController) {
    val options = listOf("Custom Broadcast Receiver", "System Battery Notification Receiver")
    val isDropdownExpanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text ="Select Broadcast Receiver Type",
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown Menu with Button and Icon
        Box {
            Button(onClick = { isDropdownExpanded.value = !isDropdownExpanded.value }) {
                Text(
                    text = if (selectedOption.value.isEmpty()) {
                        "Select"
                    } else {
                        selectedOption.value
                    }
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            }
            DropdownMenu(
                expanded = isDropdownExpanded.value,
                onDismissRequest = { isDropdownExpanded.value = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption.value = option
                            isDropdownExpanded.value = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to proceed to the next activity
        Button(
            onClick = {
                when (selectedOption.value) {
                    "Custom Broadcast Receiver" -> {
                        navController.navigate("custom_receiver")
                    }
                    "System Battery Notification Receiver" -> {
                        navController.navigate("battery_receiver")
                    }
                }
            },
            enabled = selectedOption.value.isNotEmpty() // Disable if no option selected
        ) {
            Text(text = "Next")
        }
    }
}









