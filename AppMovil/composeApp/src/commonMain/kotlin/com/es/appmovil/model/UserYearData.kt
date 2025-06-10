package com.es.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class UserYearData(
    val idEmployee:Int,
    val year:Int,
    val daysHolidays:Int,
    val currentHolidays:Int,
    var enjoyedHolidays:Int,
    var workedHours:Int,
    var recoveryHours:Int,
    var closedYear:Boolean
)