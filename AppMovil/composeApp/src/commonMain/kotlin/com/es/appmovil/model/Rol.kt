package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa un rol dentro de la organización.
 *
 * @property idRol Identificador único del rol.
 * @property rol Nombre o tipo del rol.
 */
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