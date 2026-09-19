package com.clinic.patientapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clinic.patientapp.models.DoctorDto
import com.clinic.patientapp.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    doctors: List<DoctorDto>,
    onDoctorSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = androidx.compose.foundation.rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "ClinicaCare",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryTeal
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Find a doctor, clinic, or specialization...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = SecondaryBlue,
                unfocusedBorderColor = SecondaryBlue
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Explore Specializations",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryCard("Cardiology", CardCardiology)
            CategoryCard("Dentistry", CardDentistry)
            CategoryCard("Pediatrics", CardPediatrics)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top Rated Doctors",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { }) {
                Text("See All", color = PrimaryTeal)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(doctors.filter { it.name.contains(searchQuery, ignoreCase = true) }) { doctor ->
                DoctorCard(doctor = doctor, onClick = { onDoctorSelected(doctor.id) })
            }
        }
    }
}

@Composable
fun CategoryCard(name: String, color: androidx.compose.ui.graphics.Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for icon
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DoctorCard(doctor: DoctorDto, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            coil.compose.AsyncImage(
                model = doctor.imageUrl,
                contentDescription = "Doctor Profile Picture",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SecondaryBlue)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = doctor.name, fontWeight = FontWeight.Bold)
            Text(text = doctor.specialization, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "★ ${doctor.rating}", color = androidx.compose.ui.graphics.Color(0xFFFFB300))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${doctor.reviewCount} Reviews", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Text("Book Now")
            }
        }
    }
}
