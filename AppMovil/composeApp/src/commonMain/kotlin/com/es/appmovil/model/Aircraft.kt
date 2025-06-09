package com.es.appmovil.model

import com.es.appmovil.screens.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class Aircraft(
    val idAircraft: Int,
    val desc: String
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idAircraft" to idAircraft,
        "desc" to desc
    )
    override fun getId(): String = idAircraft.toString()
}
