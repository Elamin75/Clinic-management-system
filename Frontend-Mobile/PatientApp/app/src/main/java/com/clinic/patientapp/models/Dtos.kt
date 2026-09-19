package com.clinic.patientapp.models

data class ShiftDto(
    val shiftId: String,
    val shiftName: String,
    val startTime: String,
    val endTime: String,
    val currentQueueSize: Int
)

data class RegisterForShiftRequest(
    val patientId: String,
    val shiftId: String
)

data class RegisterForShiftResponse(
    val visitId: String,
    val status: String
)

data class VisitDto(
    val visitId: String,
    val status: String,
    val queueNumber: Int?
)
