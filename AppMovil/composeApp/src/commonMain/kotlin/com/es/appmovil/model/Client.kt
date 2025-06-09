package com.es.appmovil.model

import com.es.appmovil.screens.TableEntry
import kotlinx.serialization.Serializable

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