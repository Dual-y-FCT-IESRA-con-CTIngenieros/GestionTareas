package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val clave: String,
    val valor: String
)