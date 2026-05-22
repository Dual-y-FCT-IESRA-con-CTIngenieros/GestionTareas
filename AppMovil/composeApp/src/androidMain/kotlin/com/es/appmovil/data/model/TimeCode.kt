package com.es.appmovil.data.model

data class TimeCode(
    val idTimeCode: Int,
    val desc: String,
    val color: Long,
    val chkProd: Boolean,
    val afectaEmpleado: Boolean = true,   // computa para el total del empleado
    val afectaProyecto: Boolean = true    // computa para el total del proyecto
)

