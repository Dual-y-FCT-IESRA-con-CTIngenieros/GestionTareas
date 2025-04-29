package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeWO
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    val proyects = MutableStateFlow(DataViewModel.projects)

    val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = DataViewModel.projectTimeCodes
    val projectTimeCodeDTO = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    private val _timeCodeSeleccionado = MutableStateFlow(null)
    val timeCodeSeleccionado: StateFlow<Int?> = _timeCodeSeleccionado

    private val _proyectoSeleccionado = MutableStateFlow(null)
    val proyectoSeleccionado: StateFlow<String?> = _proyectoSeleccionado

    private var _employeeActivity = MutableStateFlow(DataViewModel.employeeActivities.value)
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
        DataViewModel.employeeActivities.value.add(employeeActivity)
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
                    color = SolidColor(Color(timeCode.color.toLong()))
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
        val workOrdersPorTimeCode = mutableListOf<ProjectTimeCodeDTO>()
        val timeCodeProcesados = mutableSetOf<Int>()

        projectTimeCodes.value.forEach { code ->
            if (code.idTimeCode !in timeCodeProcesados) {
                timeCodeProcesados.add(code.idTimeCode)

                // Filtramos los proyectos que tienen este timeCode
                val proyectosAsociados = projectTimeCodes.value
                    .filter { it.idTimeCode == code.idTimeCode }
                    .map { it.idProject }

                // Obtenemos los workOrders de esos proyectos
                val workOrdersAsociados = DataViewModel.workOrders.value
                    .filter { it.idProject in proyectosAsociados }
                    .map { it.idWorkOrder }

                // Filtramos por los workOrders en los que participa el empleado
                val workOrdersEmpleado = employeeWO.value
                    .filter { it.idWorkOrder in workOrdersAsociados && it.idEmployee == employee.idEmployee }
                    .map { it.idWorkOrder }

                val dto = ProjectTimeCodeDTO(code.idTimeCode, workOrdersEmpleado.toMutableList())
                workOrdersPorTimeCode.add(dto)
            }
        }

        projectTimeCodeDTO.value = workOrdersPorTimeCode
    }

}