package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa un proyecto asociado a un cliente.
 *
 * @property idProject Identificador único del proyecto.
 * @property desc Descripción del proyecto.
 * @property idCliente Identificador del cliente asociado (puede ser nulo).
 */
@Serializable
data class Project(
    val idProject: String,
    val desc: String,
    val idCliente: Int?

) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idProject" to idProject,
        "desc" to desc,
        "idCliente" to idCliente
    )
    override fun getId(): String = idProject
}