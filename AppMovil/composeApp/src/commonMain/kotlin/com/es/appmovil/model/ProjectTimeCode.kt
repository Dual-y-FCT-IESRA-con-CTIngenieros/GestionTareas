package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectTimeCode(
    val idProject: Int,
    val idTimeCode: Int
)