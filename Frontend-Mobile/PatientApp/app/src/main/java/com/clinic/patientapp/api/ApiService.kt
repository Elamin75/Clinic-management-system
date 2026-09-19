package com.clinic.patientapp.api

import com.clinic.patientapp.models.RegisterForShiftRequest
import com.clinic.patientapp.models.RegisterForShiftResponse
import com.clinic.patientapp.models.ShiftDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import com.clinic.patientapp.models.DoctorDto

interface ApiService {
    @GET("api/doctors")
    suspend fun getDoctors(): Response<List<DoctorDto>>

    @GET("api/shifts")
    suspend fun getAvailableShifts(
        @Query("doctorId") doctorId: String,
        @Query("date") date: String
    ): Response<List<ShiftDto>>

    @POST("api/visits")
    suspend fun registerForShift(
        @Body request: RegisterForShiftRequest
    ): Response<RegisterForShiftResponse>

    @Multipart
    @POST("api/visits/{id}/receipt")
    suspend fun uploadPaymentReceipt(
        @Path("id") visitId: String,
        @Part receiptImage: MultipartBody.Part,
        @Part("transactionId") transactionId: RequestBody
    ): Response<Unit>
}
