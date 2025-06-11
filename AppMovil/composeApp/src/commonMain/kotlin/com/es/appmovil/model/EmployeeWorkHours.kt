package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa el número de de horas que tiene que trabajar un empleado en un rango de fechas.
 * (Para las jornadas parciales)
 *
 * @property dateFrom Fecha de inicio del período.
 * @property dateTo Fecha de fin del período.
 * @property idEmployee Identificador del empleado.
 * @property hours Número de horas trabajadas.
 */
@Serializable
data class EmployeeWorkHours(
    val dateFrom: String,
    val dateTo: String,
    val idEmployee: Int,
    val hours: Float
)