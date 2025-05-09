package com.es.appmovil.utils

import com.es.appmovil.model.TimeCode
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


}