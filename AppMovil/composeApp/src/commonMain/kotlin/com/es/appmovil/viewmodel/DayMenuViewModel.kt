package com.es.appmovil.viewmodel

import com.es.appmovil.model.Activity
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeWO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DayMenuViewModel {
    // Cargamos los projectsTimeCode de la base de datos
    private val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = DataViewModel.projectTimeCodes
    private val activities: StateFlow<List<Activity>> = DataViewModel.activities
    val timeCodes = DataViewModel.timeCodes

    // Variables para que se guarden las horas y el comentario
    private var _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment

    private var _hours = MutableStateFlow(8)
    val hours: StateFlow<Int> = _hours

    // Variables para la selección de TimeCodes
    private var _timeCode = MutableStateFlow(0)
    val timeCode: StateFlow<Int> = _timeCode

    private var _timeCodeSelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val timeCodeSeleccionado: StateFlow<String?> = _timeCodeSelected

    // Variables para la selección de WorkOrders
    private var _workOrder = MutableStateFlow("")
    val workOrder: StateFlow<String> = _workOrder

    private var _workSelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val workSelected: StateFlow<String?> = _workSelected

    val workOrderTimeCodeDTO = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())

    // Variables para la selección de los Activities
    private var _activity = MutableStateFlow(0)
    val activity: StateFlow<Int> = _activity

    private var _activitySelected: MutableStateFlow<String?> = MutableStateFlow(null)
    val activitySelected: StateFlow<String?> = _activitySelected

    val activityTimeCode = MutableStateFlow(mutableListOf<ProjectTimeCodeDTO>())


    fun onComment(newComment: String) {
        _comment.value = newComment
    }

    fun onHours(newHour: Int) {
        _hours.value = newHour
    }

    fun onTimeCode(newTimeCode: Int) {
        _timeCode.value = newTimeCode
    }

    fun onTimeSelected(newTimeCode: String?) {
        _timeCodeSelected.value = newTimeCode
    }

    fun onWorkOrder(newWorkOrder: String) {
        _workOrder.value = newWorkOrder
    }

    fun onWorkSelected(newProject: String?) {
        _workSelected.value = newProject
    }

    fun onActivity(newActivity: Int) {
        _activity.value = newActivity
    }

    fun onActivitySelected(newActivity: String?) {
        _activitySelected.value = newActivity
    }

    fun clear() {
        _comment.value = ""
        _hours.value = 8
        _timeCode.value = 0
        _timeCodeSelected.value = null
        _workSelected.value = null
        _activitySelected.value = null
    }

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

    fun loadTimes(code: Int) {
        val tc = timeCodes.value.find { it.idTimeCode == code }
        onTimeCode(code)
        onTimeSelected("${tc?.idTimeCode} - ${tc?.desc}")

        val indexedValue = getIndexCode(code)

        try {
            onWorkOrder(workOrderTimeCodeDTO.value[indexedValue].projects.first())
            onWorkSelected(workOrderTimeCodeDTO.value[indexedValue].projects.first())
        } catch (e: Exception) {
            print(e.message)
        }

        val a = activityTimeCode.value[indexedValue].projects.first()

        val idActivity =
            DataViewModel.activities.value.find { act -> act.idActivity.toString() == a.split("-")[0].trim() }
        val activityInt = idActivity?.idActivity ?: 0

        onActivity(activityInt)
        onActivitySelected(activityTimeCode.value[indexedValue].projects.first())
    }

    fun generateWorkOrders() {
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

        workOrderTimeCodeDTO.value =
            workOrdersPorTimeCode.sortedBy { it.idTimeCode }.toMutableList()
    }

    fun generateActivities() {
        val activitiesPorTimeCode = mutableListOf<ProjectTimeCodeDTO>()
        val timeCodeProcesados = mutableSetOf<Int>()

        activities.value.forEach { activity ->
            if (activity.idTimeCode !in timeCodeProcesados) {
                timeCodeProcesados.add(activity.idTimeCode)
                // Filtramos los activities que tienen este timeCode
                val activitiesTimeCode = activities.value
                    .filter { it.idTimeCode == activity.idTimeCode }
                    .map { "${it.idActivity} - ${it.desc}" }

                val dto =
                    ProjectTimeCodeDTO(activity.idTimeCode, activitiesTimeCode.toMutableList())
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