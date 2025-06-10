package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class Rol(
    val idRol: Int,
    val rol: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idRol" to idRol,
        "rol" to rol
    )
    override fun getId(): String = idRol.toString()
}