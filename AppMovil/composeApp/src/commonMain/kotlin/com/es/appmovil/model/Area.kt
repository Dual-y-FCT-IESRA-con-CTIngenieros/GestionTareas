package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa un área o departamento dentro de la organización.
 *
 * @property idArea Identificador único del área.
 * @property desc Descripción del área.
 */
@Serializable
data class Area(
    val idArea: Int,
    val desc: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idArea" to idArea,
        "desc" to desc
    )
    override fun getId(): String = idArea.toString()
}