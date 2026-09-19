package com.clinic.patientapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QueueTrackerScreen(
    visitId: String
) {
    // In reality, this would observe a ViewModel tied to FCM / SignalR
    var queueNumber by remember { mutableStateOf<Int?>(15) }
    var statusMessage by remember { mutableStateOf("Payment Confirmed! You are in the queue.") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (queueNumber != null) {
            Text(text = "Your Queue Number", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "#$queueNumber", 
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Waiting for Registrar to verify payment...")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = statusMessage, style = MaterialTheme.typography.bodyLarge)
    }
}
