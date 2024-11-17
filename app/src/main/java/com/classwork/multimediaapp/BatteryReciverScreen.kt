package com.classwork.multimediaapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    batteryLevel.intValue = it.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    batteryIsCharging.value =
                        it.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0
                    batteryTemperature.intValue =
                        it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
                    batteryVoltage.intValue =
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Battery Information",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BatteryInfoItem("Battery Level", "${batteryLevel.intValue}%")
                BatteryInfoItem(
                    "Charging Status",
                    if (batteryIsCharging.value) "Plugged In" else "Plugged Out"
                )
                BatteryInfoItem("Battery Temperature", "${batteryTemperature.intValue}°C")
                BatteryInfoItem("Battery Voltage", "${batteryVoltage.intValue} V")
                BatteryInfoItem("Battery Technology", batteryTechnology.value)
            }
        }
    }
}

@Composable
fun BatteryInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
