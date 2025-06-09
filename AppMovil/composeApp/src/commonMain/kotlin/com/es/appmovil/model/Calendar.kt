package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class Calendar(
    val idCalendar: Int,
    val date: String
)