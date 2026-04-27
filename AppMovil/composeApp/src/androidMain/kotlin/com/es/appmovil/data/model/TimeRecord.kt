package com.es.appmovil.data.model

data class TimeRecord(
    val id: Long? = null,
    val idEmployee: Int,
    val idWorkOrder: String? = null,
    val idTimeCode: Int? = null,
    val idActivity: Int? = null,
    val time: Float,
    val date: String,       // "YYYY-MM-DD"
    val comment: String? = null
)

