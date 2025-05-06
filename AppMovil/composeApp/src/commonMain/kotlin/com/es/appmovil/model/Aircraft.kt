package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Aircraft(
    val idAircraft: Int,
    val desc: String
)