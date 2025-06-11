package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Datos anuales del empleado relacionados con vacaciones y horas trabajadas.
 *
 * @property idEmployee Identificador del empleado.
 * @property year Año de referencia.
 * @property daysHolidays Días de vacaciones disponibles para ese año.
 * @property currentHolidays Días de vacaciones actualmente disponibles.
 * @property enjoyedHolidays Días de vacaciones ya disfrutados.
 * @property workedHours Total de horas trabajadas.
 * @property recoveryHours Horas de recuperación disponibles.
 * @property closedYear Indica si el año ya fue cerrado.
 */
@Serializable
data class UserYearData(
    val idEmployee:Int,
    val year:Int,
    val daysHolidays:Int,
    var currentHolidays:Int,
    var enjoyedHolidays:Int,
    var workedHours:Int,
    var recoveryHours:Int,
    var closedYear:Boolean
)