package com.es.appmovil.model.dto

import com.es.appmovil.interfaces.TableEntry

data class TimeCodeDTO(
    val idTimeCode: Int,
    val desc: String,
    val color: Long,
    val chkProd: Boolean
) : TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idTimeCode" to idTimeCode,
        "desc" to desc,
        "color" to color,
        "chkProd" to chkProd
    )
    override fun getId(): String = idTimeCode.toString()
}