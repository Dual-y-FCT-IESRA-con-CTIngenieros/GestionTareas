package com.es.appmovil.model

data class EmployeeWO(
    val idWorkOrder: String,
    val idEmployee:Int,
    val descripcion: String,
    val dateFrom: String,
    val dateTo: String
)