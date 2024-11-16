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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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



@Composable
fun CustomReceiverScreen(navController: NavController) {
    val inputText = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter Custom Message")
        Spacer(modifier = Modifier.height(16.dp))

        // Text input field
        OutlinedTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            label = { Text("Message") },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to send the broadcast
        Button(
            onClick = {
                if (inputText.value.isNotEmpty()) {
                    // Send the broadcast
                    val intent = Intent("com.classwork.multimediaapp.CUSTOM_BROADCAST").apply {
                        putExtra("message", inputText.value)
                        setPackage(context.packageName) // Explicitly target this app
                    }
                    context.sendBroadcast(intent)

                    // Navigate to the result screen
                    navController.navigate("custom_receiver_result")
                }
            },
            enabled = inputText.value.isNotEmpty()
        ) {
            Text(text = "Send")
        }

    }
}



@SuppressLint("NewApi")
@Composable
fun CustomReceiverResultScreen() {
    val receivedMessage = remember { mutableStateOf("No message received yet") }
    val context = LocalContext.current

    // Register and unregister the BroadcastReceiver
    DisposableEffect(Unit) {
        val customReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.classwork.multimediaapp.CUSTOM_BROADCAST") {
                    val message = intent.getStringExtra("message")
                    receivedMessage.value = message ?: "Empty message received"
                }
            }
        }
        val intentFilter = IntentFilter("com.classwork.multimediaapp.CUSTOM_BROADCAST")
        context.registerReceiver(customReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)

        onDispose {
            context.unregisterReceiver(customReceiver)
        }
    }

    // Display the received message
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Custom Broadcast Result")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Received Message:")
        Text(text = receivedMessage.value)
    }
}





@Composable
fun BatteryReceiverScreen() {
    // State variables to hold battery information
    val batteryLevel = remember { mutableIntStateOf(0) }
    val batteryIsCharging = remember { mutableStateOf(false) }
    val batteryTemperature = remember { mutableIntStateOf(0) }
    val batteryVoltage = remember { mutableIntStateOf(0) }
    val batteryTechnology = remember { mutableStateOf("Unknown") }

    // Get the context inside a composable
    val context = LocalContext.current

    // Register and unregister the BroadcastReceiver
    DisposableEffect(Unit) {
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    batteryLevel.value = it.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    batteryIsCharging.value =
                        it.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0
                    batteryTemperature.value =
                        it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
                    batteryVoltage.value =
                        it.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000
                    batteryTechnology.value =
                        it.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"
                }
            }
        }
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(batteryReceiver)
        }
    }

    // UI to display battery information
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Battery Information")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Battery Level: ${batteryLevel.value}%")
        Text(
            text = "Charging Status: ${
                if (batteryIsCharging.value) "Plugged In" else "Plugged Out"
            }"
        )
        Text(text = "Battery Temperature: ${batteryTemperature.value}°C")
        Text(text = "Battery Voltage: ${batteryVoltage.value} V")
        Text(text = "Battery Technology: ${batteryTechnology.value}")
    }
}



