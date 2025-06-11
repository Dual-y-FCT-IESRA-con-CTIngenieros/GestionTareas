package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa un código de tiempo utilizado para clasificar actividades o tareas.
 *
 * @property idTimeCode Identificador del código de tiempo.
 * @property desc Descripción del código.
 * @property color Color asociado al código.
 * @property chkProd Indica si el código se considera productivo.
 */
@Serializable
data class TimeCode(
    val idTimeCode: Int,
    val desc: String,
    val color: String,
    val chkProd: Boolean
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idTimeCode" to idTimeCode,
        "desc" to desc,
        "color" to color,
        "chkProd" to chkProd
    )
    override fun getId(): String = idTimeCode.toString()
}