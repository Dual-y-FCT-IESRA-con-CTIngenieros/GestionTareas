package com.es.appmovil.model.dto

import com.es.appmovil.interfaces.TableEntry

/**
 * DTO que representa un código de tiempo con atributos visuales y de control de producción.
 *
 * @property idTimeCode Identificador único del código de tiempo.
 * @property desc Descripción del código.
 * @property color Color asociado al código (en formato Long para hex).
 * @property chkProd Indica si está asociado a producción.
 */
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