package com.es.appmovil.model

data class EmployeeActivity(
    val idEmployee: Int,
    val idWorkOrder: Int,
    val idTimeCode: Int,
    val idActivity: Int,
    val time: Float,
    val date: String,
    val comment: String?
)