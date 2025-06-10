package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

@Serializable
data class TimeCode(
    val idTimeCode: Int,
    val desc: String,
    val color: String,
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