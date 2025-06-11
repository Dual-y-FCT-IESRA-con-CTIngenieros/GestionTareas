package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa un empleado dentro de la organización.
 *
 * @property idEmployee Identificador único del empleado.
 * @property nombre Nombre del empleado.
 * @property apellidos Apellidos del empleado.
 * @property email Correo electrónico del empleado.
 * @property dateFrom Fecha de inicio de actividad.
 * @property dateTo Fecha de finalización de actividad (puede ser nula si está activo).
 * @property idRol Identificador del rol asignado al empleado.
 * @property blockDate Fecha de bloqueo del usuario (si aplica).
 * @property idCT Identificador del centro de trabajo.
 * @property idAirbus Identificador de empleado en Airbus.
 * @property unblockDate Fecha de desbloqueo del usuario (si aplica).
 */
@Serializable
data class Employee(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    val dateTo: String?,
    val idRol: Int,
    var blockDate:String?,
    val idCT:String,
    val idAirbus:String,
    var unblockDate:String?
)