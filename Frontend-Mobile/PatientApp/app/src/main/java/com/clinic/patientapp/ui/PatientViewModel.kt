package com.clinic.patientapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clinic.patientapp.api.RetrofitClient
import com.clinic.patientapp.models.RegisterForShiftRequest
import com.clinic.patientapp.models.ShiftDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PatientViewModel : ViewModel() {

    private val _availableShifts = MutableStateFlow<List<ShiftDto>>(emptyList())
    val availableShifts: StateFlow<List<ShiftDto>> = _availableShifts

    private val _currentVisitId = MutableStateFlow<String?>(null)
    val currentVisitId: StateFlow<String?> = _currentVisitId

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchShifts(doctorId: String, date: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAvailableShifts(doctorId, date)
                if (response.isSuccessful) {
                    _availableShifts.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to fetch shifts"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun registerForShift(patientId: String, shiftId: String) {
        viewModelScope.launch {
            try {
                val request = RegisterForShiftRequest(patientId, shiftId)
                val response = RetrofitClient.apiService.registerForShift(request)
                if (response.isSuccessful) {
                    _currentVisitId.value = response.body()?.visitId
                } else {
                    _error.value = "Failed to register"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun uploadReceipt(visitId: String, imageBytes: ByteArray, transactionId: String) {
        viewModelScope.launch {
            try {
                val mediaTypeImg = "image/jpeg".toMediaTypeOrNull()
                val requestBody = imageBytes.toRequestBody(mediaTypeImg)
                val multipartBody = MultipartBody.Part.createFormData("receiptImage", "receipt.jpg", requestBody)
                
                val mediaTypeText = "text/plain".toMediaTypeOrNull()
                val transIdBody = transactionId.toRequestBody(mediaTypeText)
                
                val response = RetrofitClient.apiService.uploadPaymentReceipt(visitId, multipartBody, transIdBody)
                if (response.isSuccessful) {
                    // Success logic, UI can navigate
                } else {
                    _error.value = "Failed to upload receipt"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
