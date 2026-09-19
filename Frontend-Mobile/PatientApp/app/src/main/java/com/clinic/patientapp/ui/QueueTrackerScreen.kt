package com.clinic.patientapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.clinic.patientapp.R

@Composable
fun QueueTrackerScreen(
    visitId: String
) {
    // In reality, this would observe a ViewModel tied to FCM / SignalR
    var queueNumber by remember { mutableStateOf<Int?>(15) }
    
    val defaultStatusMessage = stringResource(R.string.payment_confirmed)
    var statusMessage by remember { mutableStateOf(defaultStatusMessage) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (queueNumber != null) {
            Text(text = stringResource(R.string.queue_title), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "#$queueNumber", 
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.waiting_verification))
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = statusMessage, style = MaterialTheme.typography.bodyLarge)
    }
}
