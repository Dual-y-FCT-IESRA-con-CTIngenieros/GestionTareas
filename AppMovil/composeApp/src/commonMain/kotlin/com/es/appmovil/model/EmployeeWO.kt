package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa una asignación de un empleado a una orden de trabajo.
 *
 * @property idWorkOrder Identificador de la orden de trabajo.
 * @property idEmployee Identificador del empleado.
 * @property desc Descripción de la asignación.
 * @property dateFrom Fecha de inicio de la asignación.
 * @property dateTo Fecha de fin de la asignación (opcional).
 */
@Serializable
data class EmployeeWO(
    val idWorkOrder: String,
    val idEmployee:Int,
    val desc: String,
    val dateFrom: String?,
    val dateTo: String?
)