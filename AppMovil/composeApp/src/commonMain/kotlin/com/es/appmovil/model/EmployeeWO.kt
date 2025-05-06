package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeWO(
    val idEmployeeWorkOrder: Int,
    val desc: String,
    val idAircraft: Int,
    val dateFrom: String,
    val dateTo: String
)