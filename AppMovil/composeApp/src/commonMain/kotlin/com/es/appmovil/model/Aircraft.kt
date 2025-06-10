package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class Aircraft(
    val idAircraft: String,
    val desc: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idAircraft" to idAircraft,
        "desc" to desc
    )
    override fun getId(): String = idAircraft.toString()
}
