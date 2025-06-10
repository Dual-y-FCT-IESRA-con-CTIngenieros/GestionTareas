package com.es.appmovil.model

import com.es.appmovil.interfaces.TableEntry
import kotlinx.serialization.Serializable

@Serializable
class Activity(
    val idActivity: Int,
    val idTimeCode: Int,
    val desc: String,
    val dateFrom: String?,
    val dateTo: String?
): TableEntry {
    override fun getFieldMap(): Map<String, Any?> = mapOf(
        "idActivity" to idActivity,
        "idTimeCode" to idTimeCode,
        "desc" to desc,
        "dateFrom" to dateFrom,
        "dateTo" to dateTo
    )

    override fun getId(): String = idActivity.toString()
}