package com.es.appmovil.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * Clase viewmodel para el calendario, donde guardaremos los datos del calendario y sus posibles funciones
 */
object CalendarViewModel {
    private var _today = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val today: StateFlow<LocalDate> = _today

    private var _year = MutableStateFlow(_today.value.year)
    val year: StateFlow<Int> = _year

    private var _month = MutableStateFlow(_today.value.month)
    val month: StateFlow<Month> = _month

    fun onMonthChangePrevious(month: DatePeriod){
        _today.value = _today.value.minus(month)
    }

    fun resetMonth(){
        _today.value = Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault()).date
    }

    fun onMonthChangeFordward(month: DatePeriod){
        _today.value = _today.value.plus(month)
    }
}