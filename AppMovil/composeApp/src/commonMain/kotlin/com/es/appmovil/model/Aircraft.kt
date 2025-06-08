package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Aircraft(
    val idAircraft: String,
    val desc: String
)