package com.es.appmovil.model.dto
import kotlinx.serialization.Serializable

/**
 * DTO utilizado para insertar un nuevo empleado en el sistema.
 *
 * @property nombre Nombre del empleado.
 * @property apellidos Apellidos del empleado.
 * @property email Correo electrónico del empleado.
 * @property dateFrom Fecha de inicio del contrato o actividad.
 * @property idRol Identificador del rol asignado.
 * @property idCT Identificador del centro de trabajo.
 * @property idAirbus Identificador del empleado en Airbus.
 * @property unblockDate Fecha opcional de desbloqueo del usuario.
 */
@Serializable
data class EmployeeInsertDTO(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val dateFrom: String,
    val idRol: Int,
    val idCT:String,
    val idAirbus:String,
    val unblockDate:String?
)