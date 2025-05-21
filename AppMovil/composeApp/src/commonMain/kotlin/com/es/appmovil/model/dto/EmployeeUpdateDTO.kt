package com.es.appmovil.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeUpdateDTO(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    var dateTo: String?,
    val idRol: Int,
    val blockDate:String?
)
