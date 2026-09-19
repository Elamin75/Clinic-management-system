package com.clinic.patientapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    viewModel: PatientViewModel,
    onShiftSelected: (String) -> Unit
) {
    val shifts by viewModel.availableShifts.collectAsState()
    val error by viewModel.error.collectAsState()

    // Trigger API fetch (In reality, doctorId and date would be selected via UI)
    LaunchedEffect(Unit) {
        viewModel.fetchShifts("doctor-uuid-123", "2026-09-19")
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Text(text = "Select a Shift", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
        }

        if (shifts.isEmpty() && error == null) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(shifts) { shift ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        onClick = { 
                            // Register the patient (using a mock patient ID for now)
                            viewModel.registerForShift("patient-uuid-456", shift.shiftId)
                            // In a real flow, we would wait for the visitId to be returned via state before navigating
                            // But for UI preview, we trigger navigation.
                            onShiftSelected("mock_visit_id") 
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = shift.shiftName, style = MaterialTheme.typography.titleMedium)
                            Text(text = "${shift.startTime} - ${shift.endTime}")
                            Text(text = "Current Queue Size: ${shift.currentQueueSize}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
