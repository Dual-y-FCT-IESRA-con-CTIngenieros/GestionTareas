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
/**
 * ViewModel para gestionar la lógica del calendario anual y datos asociados.
 */
class CalendarYearViewModel {

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _filter: MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private var _nextDaysHolidays: MutableStateFlow<Int> = MutableStateFlow(0)

    /**
     * Cambia el estado de visibilidad del diálogo.
     * @param bool true para mostrar, false para ocultar.
     */
    fun changeDialog(bool: Boolean) {
        _showDialog.value = bool
    }

    /**
     * Cambia el año del calendario restando el periodo dado.
     * @param year Periodo de años a restar (ejemplo: DatePeriod(years = 1))
     */
    fun onYearChangePrevious(year: DatePeriod) {
        today.value = today.value.minus(year)
    }

    /**
     * Cambia el año del calendario sumando el periodo dado.
     * @param year Periodo de años a sumar (ejemplo: DatePeriod(years = 1))
     */
    fun onYearChangeFordward(year: DatePeriod) {
        today.value = today.value.plus(year)
    }

    /**
     * Cambia el filtro para búsqueda o visualización.
     * @param value Nuevo valor de filtro.
     */
    fun changeFilter(value: String) {
        _filter.value = value
    }

    /**
     * Establece los días de vacaciones próximos a considerar.
     * @param days Número de días próximos.
     */
    fun setNextHolidaysDays(days: Int) {
        _nextDaysHolidays.value = days
    }

    /**
     * Obtiene los datos anuales de un empleado para el año actual.
     * @param employeeId Id del empleado.
     * @return Datos de año del empleado o null si no existen.
     */
    fun getEmployeeYearData(employeeId: Int): UserYearData? {
        val currentYear = today.value.year
        return employeesYearData.value.firstOrNull {
            it.idEmployee == employeeId && it.year == currentYear
        }
    }

    /**
     * Cierra el año actual para todos los empleados y opcionalmente genera datos para el nuevo año.
     * @param generateNewYear True para generar datos para el año siguiente.
     * @param currentYear Año actual a cerrar.
     */
    fun closeYear(generateNewYear: Boolean, currentYear: Int) {
        val blockDate = employees.value.filter { (it.blockDate ?: "") > "${_currentYear.value}/12/31" }
        if (blockDate.isNotEmpty()) {
            val nextYear = currentYear + 1
            val days = _nextDaysHolidays.value
            val hours = days * 8
            // Marca los años actuales como cerrados y actualiza en base de datos
            employeesYearData.value.map {
                it.closedYear = true
                CoroutineScope(Dispatchers.IO).launch {
                    Database.updateData("UserYearData", it)
                }
            }

            if (generateNewYear) {
                // Crea nuevos registros para el próximo año
                employees.value.forEach {
                    val enjoyedHours = employeesYearData.value.find { data -> data.idEmployee == it.idEmployee }?.enjoyedHolidays ?: 0
                    val holidaysHours = hours + (hours - enjoyedHours)
                    CoroutineScope(Dispatchers.IO).launch {
                        val employeeData = UserYearData(
                            it.idEmployee,
                            nextYear,
                            days,
                            holidaysHours,
                            0,
                            0,
                            0,
                            false
                        )
                        Database.addData("UserYearData", employeeData)
                    }
                }

                // Copia las fechas del calendario al siguiente año
                calendar.value.forEach {
                    val year = (_currentYear.value.toIntOrNull() ?: 2025) + 1
                    val newCalendar = Calendar(year, it.date)
                    CoroutineScope(Dispatchers.IO).launch {
                        Database.addData("Calendar", newCalendar)
                    }
                }

                cargarUserYearData()
            }

        } else {
            // Si no hay bloqueos, muestra diálogo de aviso
            _showDialog.value = true
        }
    }

    /**
     * Indica si el año actual está cerrado para los empleados.
     * @return True si está cerrado, false si no.
     */
    fun isCurrentYearClosed(): Boolean {
        val currentYear = today.value.year
        return employeesYearData.value.firstOrNull { it.year == currentYear }?.closedYear == true
    }

    // Función comentada para generar UserYearData (se conserva por importancia)
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