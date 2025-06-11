package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa a un manager o responsable de proyectos.
 *
 * @property idManager Identificador único del manager.
 * @property nombre Nombre del manager.
 * @property apellidos Apellidos del manager.
 */
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