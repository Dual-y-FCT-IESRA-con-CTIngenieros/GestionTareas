package com.es.appmovil.model

import com.es.appmovil.screens.TableEntry
import kotlinx.serialization.Serializable

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