package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Relación entre un proyecto y un código de tiempo.
 *
 * @property idProject Identificador del proyecto.
 * @property idTimeCode Identificador del código de tiempo.
 */
@Serializable
data class ProjectTimeCode(
    val idProject: String,
    val idTimeCode: Int
)