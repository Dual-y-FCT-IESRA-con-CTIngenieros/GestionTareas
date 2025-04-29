package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeWO(
    val idWorkOrder: String,
    val idEmployee:Int,
    val descripcion: String,
    val dateFrom: String,
    val dateTo: String
)