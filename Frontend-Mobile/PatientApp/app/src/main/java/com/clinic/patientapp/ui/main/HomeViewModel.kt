package com.clinic.patientapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clinic.patientapp.api.RetrofitClient
import com.clinic.patientapp.models.DoctorDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _doctors = MutableStateFlow<List<DoctorDto>>(emptyList())
    val doctors: StateFlow<List<DoctorDto>> = _doctors

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getDoctors()
                if (response.isSuccessful) {
                    _doctors.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load doctors"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
