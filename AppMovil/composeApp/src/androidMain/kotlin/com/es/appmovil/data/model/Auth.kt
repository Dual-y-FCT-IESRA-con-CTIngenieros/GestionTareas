package com.es.appmovil.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val idEmployee: Int,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val idRol: Int,
    val horasJornada: Int = 8,
    val horasTotalesAnuales: Int = 1792   // fallback: 8 * 224
)

