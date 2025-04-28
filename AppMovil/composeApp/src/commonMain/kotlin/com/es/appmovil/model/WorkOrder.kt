package com.es.appmovil.model

data class WorkOrder(
    val idWorkOrder: String,
    val desc: String,
    val projectManager: Int,
    val idProject: String,
    val idAircraft: Int
)