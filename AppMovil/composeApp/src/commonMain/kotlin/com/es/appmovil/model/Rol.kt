package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Rol(
    val idRol: Int,
    val rol: String
)