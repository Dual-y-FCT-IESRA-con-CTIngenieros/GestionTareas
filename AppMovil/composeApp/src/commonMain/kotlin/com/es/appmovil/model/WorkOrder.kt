package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkOrder(
    val idWorkOrder: String,
    val desc: String,
    val projectManager: Int?,
    val idProject: String,
    val idAircraft: Int?,
    val idArea:Int?
)