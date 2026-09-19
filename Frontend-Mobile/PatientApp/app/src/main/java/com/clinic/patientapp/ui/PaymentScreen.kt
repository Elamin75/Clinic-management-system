package com.clinic.patientapp.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.clinic.patientapp.R

@Composable
fun PaymentScreen(
    visitId: String,
    viewModel: PatientViewModel,
    onPaymentUploaded: () -> Unit
) {
    var transactionId by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Text(text = stringResource(R.string.upload_receipt_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = stringResource(R.string.payment_instruction))
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = transactionId,
            onValueChange = { transactionId = it },
            label = { Text(stringResource(R.string.transaction_id)) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedImageUri == null) stringResource(R.string.select_image) else stringResource(R.string.image_selected))
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                selectedImageUri?.let { uri ->
                    val bytes = readBytesFromUri(context, uri)
                    if (bytes != null) {
                        viewModel.uploadReceipt(visitId, bytes, transactionId)
                        onPaymentUploaded() 
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = transactionId.isNotEmpty() && selectedImageUri != null
        ) {
            Text(stringResource(R.string.submit_payment))
        }
    }
}

private fun readBytesFromUri(context: Context, uri: Uri): ByteArray? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    } catch (e: Exception) {
        null
    }
}
