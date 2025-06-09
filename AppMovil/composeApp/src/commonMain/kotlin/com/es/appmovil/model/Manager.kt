package com.es.appmovil.model

import com.es.appmovil.screens.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class Manager(
    val idManager: Int,
    val nombre: String,
    val apellidos: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idManager" to idManager,
        "nombre" to nombre,
        "apellidos" to apellidos
    )
    override fun getId(): String = idManager.toString()
}