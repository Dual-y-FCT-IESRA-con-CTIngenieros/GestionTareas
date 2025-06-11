package com.es.appmovil.viewmodel

import com.es.appmovil.model.Activity
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeWO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel para la gestión del menú diario de registro de horas.
 * Maneja las selecciones de time codes, work orders, activities y horas trabajadas.
 */
class DayMenuViewModel {

    // Datos globales
    private val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = DataViewModel.projectTimeCodes
    private val activities: StateFlow<List<Activity>> = DataViewModel.activities
    val timeCodes = DataViewModel.timeCodes

    // Estado: comentario diario
    private var _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment

    // Estado: horas trabajadas (por defecto 8h)
    private var _hours = MutableStateFlow(8)
    val hours: StateFlow<Int> = _hours

    // Estado: selección de TimeCode
    private var _timeCode = MutableStateFlow(0)
    val timeCode: StateFlow<Int> = _timeCode

    private var _timeCodeSelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val timeCodeSeleccionado: StateFlow<String?> = _timeCodeSelected

    // Estado: selección de WorkOrder
    private var _workOrder = MutableStateFlow("")
    val workOrder: StateFlow<String> = _workOrder

    private var _workSelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val workSelected: StateFlow<String?> = _workSelected

    // DTO para pintar los posibles workOrders por timeCode
    val workOrderTimeCodeDTO = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    // Estado: selección de Activity
    private var _activity = MutableStateFlow(0)
    val activity: StateFlow<Int> = _activity

    private var _activitySelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val activitySelected: StateFlow<String?> = _activitySelected

    // DTO para pintar las posibles actividades por timeCode
    val activityTimeCode = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    /** Setters **/
    fun onComment(newComment: String) { _comment.value = newComment }
    fun onHours(newHour: Int) { _hours.value = newHour }
    fun onTimeCode(newTimeCode: Int) { _timeCode.value = newTimeCode }
    fun onTimeSelected(newTimeCode: String?) { _timeCodeSelected.value = newTimeCode }
    fun onWorkOrder(newWorkOrder: String) { _workOrder.value = newWorkOrder }
    fun onWorkSelected(newProject: String?) { _workSelected.value = newProject }
    fun onActivity(newActivity: Int) { _activity.value = newActivity }
    fun onActivitySelected(newActivity: String?) { _activitySelected.value = newActivity }

    /**
     * Limpia todos los campos del formulario.
     */
    fun clear() {
        _comment.value = ""
        _hours.value = 8
        _timeCode.value = 0
        _timeCodeSelected.value = null
        _workSelected.value = null
        _activitySelected.value = null
    }

    /**
     * Devuelve la posición en el array de cada timeCode (mapeo fijo).
     */
    private fun getIndexCode(code: Int): Int {
        return when (code) {
            100 -> 0
            200 -> 1
            555 -> 2
            900 -> 3
            901 -> 4
            else -> 0
        }
    }

    /**
     * Carga los datos (workOrder y activity) al seleccionar un timeCode.
     */
    fun loadTimes(code: Int) {
        val tc = timeCodes.value.find { it.idTimeCode == code }
        onTimeCode(code)
        onTimeSelected("${tc?.idTimeCode} - ${tc?.desc}")

        val indexedValue = getIndexCode(code)

        try {
            onWorkOrder(workOrderTimeCodeDTO.value[indexedValue].projects.first())
            onWorkSelected(workOrderTimeCodeDTO.value[indexedValue].projects.first())
        } catch (e: Exception) {
            print(e.message) // Puede que no haya workOrder disponible
        }

        val a = activityTimeCode.value[indexedValue].projects.first()
        val idActivity = DataViewModel.activities.value
            .find { act -> act.idActivity.toString() == a.split("-")[0].trim() }
        val activityInt = idActivity?.idActivity ?: 0

        onActivity(activityInt)
        onActivitySelected(activityTimeCode.value[indexedValue].projects.first())
    }

    /**
     * Genera el listado de workOrders por timeCode, filtrando los que el empleado puede reportar.
     */
    fun generateWorkOrders() {
        val workOrdersPorTimeCode = mutableListOf<ProjectTimeCodeDTO>()
        val timeCodeProcesados = mutableSetOf<Int>()

        projectTimeCodes.value.forEach { code ->
            if (code.idTimeCode !in timeCodeProcesados) {
                timeCodeProcesados.add(code.idTimeCode)

                // Proyectos asociados a este timeCode
                val proyectosAsociados = projectTimeCodes.value
                    .filter { it.idTimeCode == code.idTimeCode }
                    .map { it.idProject }

                // WorkOrders de esos proyectos
                val workOrdersAsociados = DataViewModel.workOrders.value
                    .filter { it.idProject in proyectosAsociados }
                    .map { it.idWorkOrder }

                // Filtramos sólo los que tiene asignados el empleado
                val workOrdersEmpleado = employeeWO.value
                    .filter { it.idWorkOrder in workOrdersAsociados && it.idEmployee == employee.idEmployee }
                    .map { it.idWorkOrder }

                val dto = ProjectTimeCodeDTO(code.idTimeCode, workOrdersEmpleado.toMutableList())
                workOrdersPorTimeCode.add(dto)
            }
        }

        workOrderTimeCodeDTO.value = workOrdersPorTimeCode.sortedBy { it.idTimeCode }.toMutableList()
    }

    /**
     * Genera el listado de activities por timeCode.
     * Si el activity tiene timeCode 100, se le genera también entrada para 555.
     */
    fun generateActivities() {
        val activitiesPorTimeCode = mutableListOf<ProjectTimeCodeDTO>()
        val timeCodeProcesados = mutableSetOf<Int>()

        activities.value.forEach { activity ->
            if (activity.idTimeCode !in timeCodeProcesados) {
                timeCodeProcesados.add(activity.idTimeCode)

                val activitiesTimeCode = activities.value
                    .filter { it.idTimeCode == activity.idTimeCode }
                    .map { "${it.idActivity} - ${it.desc}" }

                val dto = ProjectTimeCodeDTO(activity.idTimeCode, activitiesTimeCode.toMutableList())
                activitiesPorTimeCode.add(dto)

                if (activity.idTimeCode == 100) {
                    val dto2 = ProjectTimeCodeDTO(555, activitiesTimeCode.toMutableList())
                    activitiesPorTimeCode.add(dto2)
                }
            }
        }

        activityTimeCode.value = activitiesPorTimeCode.sortedBy { it.idTimeCode }.toMutableList()
    }
}
