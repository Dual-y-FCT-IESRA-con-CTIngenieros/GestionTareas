package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class TimeCode(
    val idTimeCode: Int,
    val desc: String,
    val color: String,
    val chkProd: Boolean
)