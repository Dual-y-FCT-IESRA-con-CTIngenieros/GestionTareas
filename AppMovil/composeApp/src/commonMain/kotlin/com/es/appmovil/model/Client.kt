package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa un cliente.
 *
 * @property idCliente Identificador único del cliente.
 * @property nombre Nombre del cliente.
 */
@Serializable
data class Client(
    val idCliente: Int,
    val nombre: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idCliente" to idCliente,
        "nombre" to nombre
    )
    override fun getId(): String = idCliente.toString()
}