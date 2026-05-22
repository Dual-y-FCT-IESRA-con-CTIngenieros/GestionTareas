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
 * Respuesta enriquecida de GET /api/employees/{id}/hours-balance?year=YYYY
 * Incluye desglose por tipo de código de tiempo según reunión 21/05/2026.
 *
 * totalParaEmpleado = horasEstandar + permisosRetribuidos
 * totalConExtra     = totalParaEmpleado + horasExtra  (informativo, no para objetivo)
 * balance           = totalParaEmpleado + arrastreHoras - objetivoAnual
 */
data class HoursBalance(
    val horasPorCodigo: Map<String, Float> = emptyMap(),  // desglose por nombre de código
    val horasEstandar: Float = 0f,           // código 100 — afectaEmpleado=true, afectaProyecto=true
    val horasExtra: Float = 0f,              // código 555 — afectaEmpleado=false (informativo)
    val permisosRetribuidos: Float = 0f,     // código 200 — afectaEmpleado=true, afectaProyecto=false
    val otrasHoras: Float = 0f,              // código 900 — no computa
    val totalParaEmpleado: Float = 0f,       // horasEstandar + permisosRetribuidos
    val totalConExtra: Float = 0f,           // totalParaEmpleado + horasExtra (informativo)
    val jornadasAnuales: Int = 224,          // jornadas anuales leídas de Config (no hardcodeado)
    val horasJornada: Int = 8,
    val objetivoAnual: Float = 1792f,        // horasJornada * jornadasAnuales
    val arrastreVacaciones: Int = 0,
    val arrastreHoras: Int = 0,
    val balance: Float = 0f                  // totalParaEmpleado + arrastreHoras - objetivoAnual
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

