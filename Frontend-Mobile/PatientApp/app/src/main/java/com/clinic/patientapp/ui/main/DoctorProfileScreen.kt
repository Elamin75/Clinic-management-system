package com.clinic.patientapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.clinic.patientapp.models.DoctorDto
import com.clinic.patientapp.theme.*
import com.clinic.patientapp.ui.PatientViewModel

@Composable
fun DoctorProfileScreen(
    doctor: DoctorDto,
    viewModel: PatientViewModel,
    onBookShift: (String) -> Unit
) {
    val shifts by viewModel.availableShifts.collectAsState()

    LaunchedEffect(doctor.id) {
        viewModel.fetchShifts(doctor.id, "2026-09-19")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryBlue)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(BackgroundWhite)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = doctor.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = doctor.specialization,
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryTeal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = doctor.bio,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = BackgroundWhite
        ) {
            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(shifts) { shift ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(text = "${shift.shiftName} (Today)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "🕘 ${shift.startTime} - ${shift.endTime}", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "👥 Queue: ${shift.currentQueueSize} Patients", style = MaterialTheme.typography.bodyLarge)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { onBookShift(shift.shiftId) },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                            ) {
                                Text("Book Queue Slot")
                            }
                        }
                    }
                }
            }
        }
    }
}
