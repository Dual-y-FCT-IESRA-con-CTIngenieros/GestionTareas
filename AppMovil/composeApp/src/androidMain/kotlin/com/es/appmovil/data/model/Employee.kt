package com.es.appmovil.data.model

/**
 * Datos del empleado tal como los devuelve la API.
 * horasJornada: jornada diaria en horas (libre, sin límite fijo).
 * horasTotalesAnuales: calculado en backend como horasJornada * 224.
 */
data class Employee(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val idRol: Int,
    val horasJornada: Int = 8,
    val horasTotalesAnuales: Int = 1792   // fallback: 8 * 224
)

/**
 * Respuesta de GET /api/employees/{id}/hours-balance.
 * objetivoAnual es calculado por el backend: horasJornada * 224.
 */
data class HoursBalance(
    val idEmployee: Int,
    val horasRegistradas: Float,
    val objetivoAnual: Int,
    val diferencia: Float         // horasRegistradas - objetivoAnual (negativo = por debajo)
)

/**
 * Body para crear empleado: POST /api/employees
 */
data class CreateEmployeeRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    val idRol: Int,
    val idCT: String = "",
    val idAirbus: String = "",
    val horasJornada: Int = 8
)

/**
 * Body para editar empleado: PUT /api/employees
 */
data class UpdateEmployeeRequest(
    val idEmployee: Int,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val horasJornada: Int = 8
)

/**
 * Body para registro de usuario: POST /api/auth/register
 */
data class RegisterRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val id: Int,
    val password: String,
    val horasJornada: Int = 8
)

