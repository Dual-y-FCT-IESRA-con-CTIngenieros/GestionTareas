package com.es.appmovil.model

import com.es.appmovil.screens.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class WorkOrder(
    val idWorkOrder: String,
    val desc: String,
    val projectManager: Int?,
    val idProject: String,
    val idAircraft: Int?,
    val idArea: Int?
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idWorkOrder" to idWorkOrder,
        "desc" to desc,
        "projectManager" to projectManager,
        "idProject" to idProject,
        "idAircraft" to idAircraft,
        "idArea" to idArea
    )
    override fun getId(): String = idWorkOrder
}