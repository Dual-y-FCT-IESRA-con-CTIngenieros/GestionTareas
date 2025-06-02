package com.es.appmovil.viewmodel

import com.es.appmovil.database.Database
import com.es.appmovil.model.Calendar
import com.es.appmovil.model.UserYearData
import com.es.appmovil.viewmodel.DataViewModel._currentYear
import com.es.appmovil.viewmodel.DataViewModel.calendar
import com.es.appmovil.viewmodel.DataViewModel.cargarUserYearData
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.employeesYearData
import com.es.appmovil.viewmodel.DataViewModel.today
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class CalendarYearViewModel {

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _filter:MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private var _nextDaysHolidays: MutableStateFlow<Int> = MutableStateFlow(0)

    fun changeDialog(bool:Boolean) {
        _showDialog.value = bool
    }

    /**
     * Función para cambiar el año que se muestra en el calendario
     * @param year Numero de años que se van a cambiar hacia atras
     */
    fun onYearChangePrevious(year: DatePeriod) {
        today.value = today.value.minus(year)
    }

    /**
     * Función para cambiar el año que se muestra en el calendario
     * @param year Numero de años que se van a cambiar hacia delante
     */
    fun onYearChangeFordward(year: DatePeriod) {
        today.value = today.value.plus(year)
    }

    fun changeFilter(value:String) {
        _filter.value = value
    }

    fun setNextHolidaysDays(days: Int) {
        _nextDaysHolidays.value = days
    }

    fun getEmployeeYearData(employeeId: Int): UserYearData? {
        val currentYear = today.value.year
        return employeesYearData.value.firstOrNull {
            it.idEmployee == employeeId && it.year == currentYear
        }
    }

    fun closeYear(generateNewYear:Boolean){

        val blockDate = employees.value.filter { (it.blockDate ?: "") > "${_currentYear.value}/12/31" }
        if (blockDate.isNotEmpty()) {
            val currentYear = _currentYear.value.toIntOrNull() ?: 0
            val nextYear = currentYear + 1
            val days = _nextDaysHolidays.value
            val hours = days * 8
            employeesYearData.value.map {
                it.closedYear = true
                CoroutineScope(Dispatchers.IO).launch {
                    Database.updateData("UserYearData", it)
                }
            }

            if (generateNewYear) {
                employees.value.forEach {
                    val enjooyedHours = employeesYearData.value.find {data -> data.idEmployee == it.idEmployee }?.enjoyedHolidays ?: 0
                    val holidaysHours = hours + (hours - enjooyedHours)
                    CoroutineScope(Dispatchers.IO).launch {
                        val employeeData = UserYearData(it.idEmployee, nextYear, days, holidaysHours, 0, 0, 0, false)
                        Database.addData("UserYearData", employeeData)
                    }
                }

                calendar.value.forEach {
                    val year = (_currentYear.value.toIntOrNull() ?: 2025)+ 1
                    val newCalendar = Calendar(year, it.date)
                    CoroutineScope(Dispatchers.IO).launch {
                        Database.addData("Calendar", newCalendar)
                    }
                }

                cargarUserYearData()
            }

        } else {
            _showDialog.value = true
        }

    }
    fun isCurrentYearClosed(): Boolean {
        val currentYear = today.value.year
        return employeesYearData.value.firstOrNull { it.year == currentYear }?.closedYear == true
    }





//    fun generateUserYearData() { NO BORRAR AÚN FUNCION IMPORTANTE PARA GENERAR LOS USERYEARDATA
//        val daysHolidays = 23
//        val currentHolidays = daysHolidays * 8
//
//        // Agrupamos por empleado y año
//        val groupedData = employeeActivities.value.groupBy { it.idEmployee to LocalDate.parse(it.date).year }
//
//        val userYearData = groupedData.map { (key, entries) ->
//            val (idEmployee, year) = key
//
//            val enjoyedHolidays = entries
//                .filter { it.idTimeCode == 900 }
//                .sumOf { it.time.toInt() }
//
//            val workedHours = entries
//                .filter { it.idTimeCode == 100 }
//                .sumOf { it.time.toInt() }
//
//            val recoveryHours = (workedHours - 1792).coerceAtLeast(0)
//
//            val closedYear = year.toString() != _currentYear.value
//
//            UserYearData(
//                idEmployee = idEmployee,
//                year = year,
//                daysHolidays = daysHolidays,
//                currentHolidays = currentHolidays,
//                enjoyedHolidays = enjoyedHolidays,
//                workedHours = workedHours,
//                recoveryHours = recoveryHours,
//                closedYear = closedYear
//            )
//        }
//
//        userYearData.forEach {
//            CoroutineScope(Dispatchers.IO).launch {
//                Database.addData("UserYearData", it)
//            }
//        }
//    }

}