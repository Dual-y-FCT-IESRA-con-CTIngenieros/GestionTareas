package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeWorkHours(
    val dateFrom: String,
    val dateTo: String,
    val idEmployee: Int,
    val hours: Float
)