package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkOrder(
    val idWorkOrder: Int,
    val desc: String,
    val projectManager: Int,
    val idProject: Int,
    val idEmployeeWO: Int
)