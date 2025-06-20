package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.es.appmovil.database.Database
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.changeMonth
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.employeesYearData
import com.es.appmovil.viewmodel.DataViewModel.getPie
import com.es.appmovil.viewmodel.DataViewModel.today
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * ViewModel para gestionar el calendario, actividades y visualización de barras por día.
 */
class CalendarViewModel {

    private val _bars = MutableStateFlow<List<Bars>>(emptyList())
    val bars: StateFlow<List<Bars>> = _bars

    val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    private var _employeeActivity = MutableStateFlow(employeeActivities.value)
    val employeeActivity: StateFlow<List<EmployeeActivity>> = _employeeActivity

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _showDialogConfig = MutableStateFlow(false)
    val showDialogConfig: StateFlow<Boolean> = _showDialogConfig

    private var _reset = MutableStateFlow(false)

    /**
     * Cambia la visibilidad del diálogo principal.
     */
    fun changeDialog(bool: Boolean) {
        _showDialog.value = bool
    }

    /**
     * Cambia la visibilidad del diálogo de configuración.
     */
    fun changeDialogConfig(bool: Boolean) {
        _showDialogConfig.value = bool
    }

    /**
     * Cambia el mes mostrado hacia atrás y actualiza datos y gráficos.
     * @param month Periodo de meses a restar.
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        today.value = today.value.minus(month)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
        _reset.value = true
    }

    /**
     * Cambia el mes mostrado hacia adelante y actualiza datos y gráficos.
     * @param month Periodo de meses a sumar.
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        today.value = today.value.plus(month)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
        _reset.value = true
    }

    /**
     * Obtiene las actividades del empleado para una fecha dada.
     * @param fechaSeleccionada Fecha a consultar.
     * @return Lista de actividades para el empleado y fecha.
     */
    fun getActivitiesForDate(fechaSeleccionada: LocalDate): List<EmployeeActivity> {
        return employeeActivity.value.filter {
            LocalDate.parse(it.date) == fechaSeleccionada && it.idEmployee == employee.idEmployee
        }
    }

    /**
     * Resetea las barras de datos para mostrar gráfico vacío.
     */
    private fun resetBars() {
        _bars.value = listOf(
            Bars(
                label = "",
                values = listOf(
                    Bars.Data(
                        label = "",
                        value = 0.0,
                        color = SolidColor(Color.Black)
                    )
                )
            )
        )
    }

    /**
     * Añade o actualiza una actividad de empleado y actualiza base de datos y datos relacionados.
     */
    fun addEmployeeActivity(employeeActivity: EmployeeActivity) {
        val filtro =
            employeeActivities.value.find { it.date == employeeActivity.date && it.idTimeCode == employeeActivity.idTimeCode }
        if (filtro == null) {
            if (employeeActivity.time != 0f) {
                employeeActivities.value.add(employeeActivity)
                CoroutineScope(Dispatchers.IO).launch {
                    Database.addEmployeeActivity(employeeActivity)
                }
            }
        } else {
            employeeActivities.value.remove(filtro)
            if (employeeActivity.time != 0f) {
                employeeActivities.value.add(employeeActivity)
                CoroutineScope(Dispatchers.IO).launch {
                    Database.updateEmployeeActivity(employeeActivity)
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    Database.deleteEmployeeActivity(employeeActivity)
                }
            }
        }

        if (employeeActivity.idTimeCode == 900) { // Código para vacaciones
            val userYearData = employeesYearData.value.find { it.idEmployee == employee.idEmployee }
            if (userYearData != null) {
                userYearData.enjoyedHolidays += employeeActivity.time.toInt()
                userYearData.currentHolidays -= employeeActivity.time.toInt()
                CoroutineScope(Dispatchers.IO).launch {
                    Database.updateData("UserYearData", userYearData)
                }
            }
        } else if (employeeActivity.idEmployee == 100) { // Código para horas trabajadas
            val userYearData = employeesYearData.value.find { it.idEmployee == employee.idEmployee }
            if (userYearData != null) {
                userYearData.workedHours += employeeActivity.time.toInt()
                CoroutineScope(Dispatchers.IO).launch {
                    Database.updateData("UserYearData", userYearData)
                }
            }
        }

        getPie()
    }

    /**
     * Genera las barras para un día específico basado en las actividades del empleado.
     * @param fechaSeleccionada Fecha a mostrar.
     */
    fun generarBarrasPorDia(fechaSeleccionada: LocalDate) {
        val timeCodeMap = timeCodes.value.associateBy { it.idTimeCode }

        val actividadesDelDia = employeeActivity.value.filter {
            LocalDate.parse(it.date) == fechaSeleccionada && it.idEmployee == employee.idEmployee
        }

        if (actividadesDelDia.isEmpty() || _reset.value) {
            resetBars()
            _reset.value = false
            return
        }

        val dataPorTimeCode = actividadesDelDia.groupBy { it.idTimeCode }
            .mapNotNull { (idTimeCode, listaActividades) ->
                val timeCode = timeCodeMap[idTimeCode] ?: return@mapNotNull null
                Bars.Data(
                    label = timeCode.desc,
                    value = listaActividades.sumOf { it.time.toDouble() },
                    color = SolidColor(Color(timeCode.color))
                )
            }

        _bars.value = listOf(
            Bars(
                label = fechaSeleccionada.toString(),
                values = dataPorTimeCode
            )
        )
    }
}
