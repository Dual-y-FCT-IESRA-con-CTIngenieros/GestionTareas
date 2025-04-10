package com.es.appmovil.model

data class Employee(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    val dateTo: String?,
    val idRol: Int
)