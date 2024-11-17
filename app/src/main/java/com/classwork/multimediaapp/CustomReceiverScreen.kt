package com.classwork.multimediaapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun CustomReceiverScreen(navController: NavController) {
    val inputText = remember { mutableStateOf("") }
    val context = LocalContext.current
    val navigateToResult = remember { mutableStateOf(false) }

    // Trigger navigation to the result screen
    if (navigateToResult.value) {
        LaunchedEffect(Unit) {
            delay(100)
            navController.navigate("custom_receiver_result")
            navigateToResult.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter Custom Message")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (inputText.value.isNotEmpty()) {
                    val intent = Intent("com.classwork.multimediaapp.CUSTOM_BROADCAST").apply {
                        putExtra("com.classwork.multimediaapp.EXTRA_DATA", inputText.value)
                    }
                    context.sendBroadcast(intent) // Sends broadcast to all apps
                    Toast.makeText(context, "Broadcast sent!", Toast.LENGTH_SHORT).show()
                    navigateToResult.value = true
                } else {
                    Toast.makeText(context, "Enter a message", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = inputText.value.isNotEmpty()
        ) {
            Text("Send")
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun CustomReceiverResultScreen() {
    // State variable for received message
    val receivedMessage = remember { mutableStateOf("No message received yet") }
    val context = LocalContext.current
    val refreshTrigger = remember { mutableStateOf(false) } // Tracks refresh requests

    // Register and unregister the BroadcastReceiver
    DisposableEffect(refreshTrigger.value) {
        val customReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.classwork.multimediaapp.CUSTOM_BROADCAST") {
                    val message = intent.getStringExtra("com.classwork.multimediaapp.EXTRA_DATA")
                    receivedMessage.value = message ?: "Empty message received"
                    Toast.makeText(context, "Message Received: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val intentFilter = IntentFilter("com.classwork.multimediaapp.CUSTOM_BROADCAST")
        context.registerReceiver(customReceiver, intentFilter, Context.RECEIVER_EXPORTED)

        onDispose {
            context.unregisterReceiver(customReceiver)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Custom Broadcast Result")
        Spacer(Modifier.height(16.dp))
        Text("Received Message:")
        Text(receivedMessage.value)
        Spacer(Modifier.height(16.dp))

        // Refresh Button
        androidx.compose.material3.Button(
            onClick = {
                receivedMessage.value = "No message received yet" // Reset the message
                refreshTrigger.value = !refreshTrigger.value // Trigger re-registration
                Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Refresh")
        }
    }
}


