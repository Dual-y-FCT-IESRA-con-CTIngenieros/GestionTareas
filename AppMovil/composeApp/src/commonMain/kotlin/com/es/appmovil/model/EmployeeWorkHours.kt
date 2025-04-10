package com.es.appmovil.model

data class EmployeeWorkHours(
    val dateFrom: String,
    val dateTo: String,
    val idEmployee: Int,
    val hours: Float
)