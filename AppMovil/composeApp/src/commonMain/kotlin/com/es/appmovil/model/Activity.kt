package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa una actividad realizada, asociada a un código de tiempo.
 *
 * @property idActivity Identificador único de la actividad.
 * @property idTimeCode Código de tiempo asociado.
 * @property desc Descripción de la actividad.
 * @property dateFrom Fecha de inicio (opcional).
 * @property dateTo Fecha de finalización (opcional).
 */
@Serializable
class Activity(
    val idActivity: Int,
    val idTimeCode: Int,
    val desc: String,
    val dateFrom: String?,
    val dateTo: String?
): TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idActivity" to idActivity,
        "idTimeCode" to idTimeCode,
        "desc" to desc,
        "dateFrom" to dateFrom,
        "dateTo" to dateTo
    )

    override fun getId(): String = idActivity.toString()
}