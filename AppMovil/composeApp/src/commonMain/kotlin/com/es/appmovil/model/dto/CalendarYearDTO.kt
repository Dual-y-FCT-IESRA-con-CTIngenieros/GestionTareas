package com.es.appmovil.model.dto

/**
 * DTO que representa un conjunto de fechas asociadas a un identificador de calendario.
 *
 * @property idCalendar Identificador único del calendario.
 * @property date Lista de fechas asociadas en formato YYYY-MM-DD.
 */
data class CalendarYearDTO(
    val idCalendar: Int,
    val date: List<String>
)