package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.forEach
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
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

    private val _bars = MutableStateFlow<List<Bars>>(emptyList())
    val bars: StateFlow<List<Bars>> = _bars

    val timeCodes = MutableStateFlow(DataViewModel.timeCodes)

    val proyects = MutableStateFlow(DataViewModel.proyects)

    val projectTimeCodes = MutableStateFlow(DataViewModel.proyectTimecodes)
    val projectTimeCodeDTO = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    private val _timeCodeSeleccionado = MutableStateFlow(null)
    val timeCodeSeleccionado: StateFlow<Int?> = _timeCodeSeleccionado

    private val _proyectoSeleccionado = MutableStateFlow(null)
    val proyectoSeleccionado: StateFlow<String?> = _proyectoSeleccionado

    private var _employeeActivity = MutableStateFlow(DataViewModel.employeeActivities)
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

    fun generarBarrasPorDia(fechaSeleccionada: LocalDate) {
        val timeCodeMap = timeCodes.value.associateBy { it.idTimeCode }

        val actividadesDelDia = employeeActivity.value.filter {
            LocalDate.parse(it.date) == fechaSeleccionada
        }

        if (actividadesDelDia.isEmpty()) {
            _bars.value = listOf(Bars(
                label = "",
                values = listOf(
                    Bars.Data(
                        label = "",
                        value = 0.0,
                        color = SolidColor(Color.Black)
                    )
                )
            ))
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
                label = fechaSeleccionada.dayOfMonth.toString(),
                values = dataPorTimeCode
            )
        )
    }

    fun generarProjectsTimeCode() {
        var timeCode = 0
        projectTimeCodes.value.forEach { code ->
            if (code.idTimeCode != timeCode){
                val projects = projectTimeCodes.value.filter { it.idTimeCode == code.idTimeCode }
                val projectTimeCode = ProjectTimeCodeDTO(code.idTimeCode, mutableListOf())
                projects.map{ projectTimeCode.projects.add(it.idProject) }
                timeCode = code.idTimeCode
                projectTimeCodeDTO.value.add(projectTimeCode)
            }
        }
    }
}