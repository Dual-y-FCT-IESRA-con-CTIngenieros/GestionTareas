package com.es.appmovil.data.remote

import com.es.appmovil.data.model.Activity
import com.es.appmovil.data.model.LoginRequest
import com.es.appmovil.data.model.LoginResponse
import com.es.appmovil.data.model.TimeCode
import com.es.appmovil.data.model.TimeRecord
import com.es.appmovil.data.model.WorkOrder
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ────────── Auth ──────────
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // ────────── Registros de horas ──────────
    @GET("employee-activities")
    suspend fun getAllActivities(): List<TimeRecord>

    @GET("employee-activities/by-employee/{id}")
    suspend fun getActivitiesByEmployee(@Path("id") idEmployee: Int): List<TimeRecord>

    @POST("employee-activities")
    suspend fun createActivity(@Body record: TimeRecord): TimeRecord

    @PUT("employee-activities")
    suspend fun updateActivity(@Body record: TimeRecord): TimeRecord

    @HTTP(method = "DELETE", path = "employee-activities", hasBody = true)
    suspend fun deleteActivity(@Body record: TimeRecord)

    // ────────── Órdenes de trabajo ──────────
    @GET("workorders")
    suspend fun getWorkOrders(): List<WorkOrder>

    // ────────── Códigos de tiempo ──────────
    @GET("timecodes")
    suspend fun getTimeCodes(): List<TimeCode>

    // ────────── Actividades ──────────
    @GET("activities")
    suspend fun getActivities(): List<Activity>
}

