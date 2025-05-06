package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.es.appmovil.database.Database
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.changeMonth
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.employeeWO
import com.es.appmovil.viewmodel.DataViewModel.getPie
import com.es.appmovil.viewmodel.DataViewModel.today
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _bars = MutableStateFlow<List<Bars>>(emptyList())
    val bars: StateFlow<List<Bars>> = _bars

    val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    private val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = DataViewModel.projectTimeCodes
    val projectTimeCodeDTO = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    private var _employeeActivity = MutableStateFlow(employeeActivities.value)
    val employeeActivity: StateFlow<List<EmployeeActivity>> = _employeeActivity

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

//    val proyects = MutableStateFlow(DataViewModel.projects)
//    private val _timeCodeSeleccionado = MutableStateFlow(null)
//    val timeCodeSeleccionado: StateFlow<Int?> = _timeCodeSeleccionado
//
//    private val _proyectoSeleccionado = MutableStateFlow(null)
//    val proyectoSeleccionado: StateFlow<String?> = _proyectoSeleccionado

    fun changeDialog(bool: Boolean) {
        _showDialog.value = bool
    }


    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia atras
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        today.value = today.value.minus(month)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
    }

    /**
     * Funcion para cambiar el mes al actual
     */
    fun resetMonth() {
        today.value = Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).date
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia delante
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        today.value = today.value.plus(month)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
    }

    fun addEmployeeActivity(employeeActivity: EmployeeActivity){
        val filtro = employeeActivities.value.find { it.date == employeeActivity.date && it.idTimeCode == employeeActivity.idTimeCode }
        if (filtro == null) {
            if (employeeActivity.time != 0f) {
                employeeActivities.value.add(employeeActivity)
                CoroutineScope(Dispatchers.IO).launch {
                    Database.addEmployeeActivity(employeeActivity)
                }
            }
        }
        else {
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
        getPie()
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