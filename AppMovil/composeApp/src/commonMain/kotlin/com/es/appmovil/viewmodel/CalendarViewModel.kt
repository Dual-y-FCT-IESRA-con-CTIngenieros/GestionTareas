package com.es.appmovil.viewmodel

import com.es.appmovil.model.Activity
import com.es.appmovil.model.EmployeeActivity
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
class CalendarViewModel {
    private var _today =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val today: StateFlow<LocalDate> = _today

    private var _timeCode = MutableStateFlow(
        mutableMapOf<Int, String>(
            100 to "Normal hours",
            200 to "Non productive hours",
            555 to "Extra hours",
            900 to "Vacations hours",
            901 to "Compensation hours"
        )
    )
    val timeCode: StateFlow<Map<Int, String>> = _timeCode

    private var _employeeActivity = MutableStateFlow(mutableListOf<EmployeeActivity>())
    val employeeActivity: StateFlow<List<EmployeeActivity>> = _employeeActivity

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia atras
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        _today.value = _today.value.minus(month)
    }

    /**
     * Funcion para cambiar el mes al actual
     */
    fun resetMonth() {
        _today.value = Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia delante
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        _today.value = _today.value.plus(month)
    }

    fun addEmployeeActivity(employeeActivity: EmployeeActivity){
        print(_employeeActivity)
        _employeeActivity.value = _employeeActivity.value.toMutableList().apply {
            add(employeeActivity)
        }
    }
}