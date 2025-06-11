package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

/**
 * Representa una orden de trabajo dentro de un proyecto.
 *
 * @property idWorkOrder Identificador único de la orden.
 * @property desc Descripción de la orden de trabajo.
 * @property projectManager Identificador del manager asociado (opcional).
 * @property idProject Proyecto al que pertenece la orden.
 * @property idAircraft Identificador de la aeronave asociada (opcional).
 * @property idArea Área asociada a la orden (opcional).
 */
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