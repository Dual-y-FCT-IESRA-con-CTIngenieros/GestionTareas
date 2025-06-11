package com.es.appmovil.model

import kotlinx.serialization.Serializable

/**
 * Representa una configuración del sistema almacenada en la base de datos.
 *
 * @property clave Clave de configuración única.
 * @property valor Valor asociado a la clave de configuración.
 */
@Serializable
data class Config(
    val clave: String,
    val valor: String
)