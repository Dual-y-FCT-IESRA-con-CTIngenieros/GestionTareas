package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa una entrada en el calendario (por ejemplo, día laborable, festivo, etc.).
 *
 * @property idCalendar Identificador único del evento o marca en el calendario.
 * @property date Fecha correspondiente al evento en formato YYYY-MM-DD.
 */
@Serializable
data class Calendar(
    val idCalendar: Int,
    val date: String
)