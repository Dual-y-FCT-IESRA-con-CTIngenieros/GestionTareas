package com.es.appmovil.model.dto
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeInsertDTO(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    val idRol: Int,
    val idCT: String,
    val idAirbus: String,
)