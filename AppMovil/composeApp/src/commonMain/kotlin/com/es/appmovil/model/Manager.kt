package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Manager(
    val idManager: Int,
    val nombre: String,
    val apellidos: String
)