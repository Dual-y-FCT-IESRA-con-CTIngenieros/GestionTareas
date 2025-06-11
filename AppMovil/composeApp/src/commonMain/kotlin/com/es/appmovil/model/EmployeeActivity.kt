package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa una actividad realizada por un empleado en un día concreto.
 *
 * @property idEmployee Identificador del empleado que realizó la actividad.
 * @property idWorkOrder Orden de trabajo asociada.
 * @property idTimeCode Código de tiempo usado.
 * @property idActivity Actividad específica realizada.
 * @property time Tiempo dedicado en horas.
 * @property date Fecha de la actividad.
 * @property comment Comentarios adicionales (opcional).
 */
@Serializable
data class EmployeeActivity(
    val idEmployee: Int,
    val idWorkOrder: String,
    val idTimeCode: Int,
    val idActivity: Int,
    val time: Float,
    val date: String,
    val comment: String?
)