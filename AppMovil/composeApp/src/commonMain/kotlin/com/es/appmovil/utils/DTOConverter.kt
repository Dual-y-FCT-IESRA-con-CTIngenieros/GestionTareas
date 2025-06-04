package com.es.appmovil.utils

import com.es.appmovil.model.Employee
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.dto.EmployeeInsertDTO
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.model.dto.TimeCodeDTO

object DTOConverter {

    fun TimeCode.toDTO(): TimeCodeDTO {
        return TimeCodeDTO(
            idTimeCode = idTimeCode,
            desc = desc,
            color = color.removePrefix("0x").toULong(16).toLong(),
            chkProd = chkProd
        )
    }
    fun TimeCodeDTO.toEntity(): TimeCode {
        return TimeCode(
            idTimeCode = idTimeCode,
            desc = desc,
            color = "0x" + color.toString(16).uppercase(),
            chkProd = chkProd
        )
    }

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