package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val idProject: String,
    val desc: String,
    val idCliente: Int,
    val idArea: Int,
    val idAircraft: Int
)