package com.es.appmovil.model.dto

import kotlinx.serialization.Serializable

/**
 * DTO utilizado para actualizar los datos de un empleado existente.
 *
 * @property idEmployee Identificador único del empleado.
 * @property nombre Nombre del empleado.
 * @property apellidos Apellidos del empleado.
 * @property email Correo electrónico del empleado.
 * @property dateFrom Fecha de inicio del contrato o actividad.
 * @property dateTo Fecha de finalización (opcional).
 * @property idRol Rol asignado.
 * @property blockDate Fecha en que fue bloqueado (opcional).
 * @property idCT Identificador del centro de trabajo.
 * @property idAirbus Identificador del empleado en Airbus.
 * @property unblockDate Fecha de desbloqueo (opcional).
 */
@Serializable
data class EmployeeUpdateDTO(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    var dateTo: String?,
    val idRol: Int,
    val blockDate:String?,
    val idCT:String,
    val idAirbus:String,
    val unblockDate:String?
)
