package com.es.appmovil.utils

import com.es.appmovil.model.Employee
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.dto.EmployeeInsertDTO
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.model.dto.TimeCodeDTO

/**
 * Objeto que provee funciones de conversión entre entidades de dominio y DTOs,
 * facilitando la transformación de datos para persistencia o comunicación externa.
 */
object DTOConverter {

    /**
     * Convierte un objeto [TimeCode] a su correspondiente DTO [TimeCodeDTO].
     * Convierte el color de String hexadecimal a Long.
     *
     * @receiver [TimeCode] entidad de dominio.
     * @return [TimeCodeDTO] Data Transfer Object.
     */
    fun TimeCode.toDTO(): TimeCodeDTO {
        return TimeCodeDTO(
            idTimeCode = idTimeCode,
            desc = desc,
            color = color.removePrefix("0x").toULong(16).toLong(),
            chkProd = chkProd
        )
    }

    /**
     * Convierte un objeto [TimeCodeDTO] a su correspondiente entidad [TimeCode].
     * Convierte el color de Long a String hexadecimal con prefijo "0x".
     *
     * @receiver [TimeCodeDTO] Data Transfer Object.
     * @return [TimeCode] entidad de dominio.
     */
    fun TimeCodeDTO.toEntity(): TimeCode {
        return TimeCode(
            idTimeCode = idTimeCode,
            desc = desc,
            color = "0x" + color.toString(16).uppercase(),
            chkProd = chkProd
        )
    }

    /**
     * Convierte un objeto [Employee] a su DTO para inserción [EmployeeInsertDTO].
     * Omite campos no necesarios para la inserción (como id).
     *
     * @receiver [Employee] entidad de dominio.
     * @return [EmployeeInsertDTO] DTO para inserción.
     */
    fun Employee.toInsertDTO(): EmployeeInsertDTO {
        return EmployeeInsertDTO(
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            dateFrom = dateFrom,
            idRol = idRol,
            idCT = idCT,
            idAirbus = idAirbus,
            unblockDate = unblockDate
        )
    }

    /**
     * Convierte un objeto [EmployeeUpdateDTO] a su correspondiente entidad [Employee].
     * Incluye todos los campos necesarios para actualización.
     *
     * @receiver [EmployeeUpdateDTO] DTO para actualización.
     * @return [Employee] entidad de dominio.
     */
    fun EmployeeUpdateDTO.toEntity(): Employee {
        return Employee(
            idEmployee = idEmployee,
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            dateFrom = dateFrom,
            dateTo = dateTo,
            idRol = idRol,
            blockDate = blockDate,
            idCT = idCT,
            idAirbus = idAirbus,
            unblockDate = unblockDate
        )
    }
}