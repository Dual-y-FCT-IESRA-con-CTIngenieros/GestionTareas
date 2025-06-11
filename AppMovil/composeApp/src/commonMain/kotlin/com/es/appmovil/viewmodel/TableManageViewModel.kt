package com.es.appmovil.viewmodel

import androidx.lifecycle.ViewModel
import com.es.appmovil.model.*
import com.es.appmovil.model.dto.TimeCodeDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la gestión de las tablas maestras (entidades estáticas).
 *
 * Este ViewModel carga una copia de los datos obtenidos desde el DataViewModel,
 * permitiendo trabajar sobre estos sin afectar directamente al resto de la aplicación.
 */
class TableManageViewModel : ViewModel() {

    // Lista de actividades cargadas
    private val _activities: MutableStateFlow<MutableList<Activity>> =
        MutableStateFlow(DataViewModel.activities.value.toMutableList())
    val activities: StateFlow<MutableList<Activity>> = _activities.asStateFlow()

    // Lista de aeronaves cargadas
    private val _aircraft: MutableStateFlow<MutableList<Aircraft>> =
        MutableStateFlow(DataViewModel.aircraft.value.toMutableList())
    val aircraft: StateFlow<MutableList<Aircraft>> = _aircraft.asStateFlow()

    // Lista de áreas cargadas
    private val _area: MutableStateFlow<MutableList<Area>> =
        MutableStateFlow(DataViewModel.areas.value.toMutableList())
    val area: StateFlow<MutableList<Area>> = _area.asStateFlow()

    // Lista de calendario (días laborales)
    private val _calendar: MutableStateFlow<MutableList<Calendar>> =
        MutableStateFlow(DataViewModel.calendar.value.toMutableList())
    val calendar: StateFlow<MutableList<Calendar>> = _calendar.asStateFlow()

    // Lista de clientes
    private val _client: MutableStateFlow<MutableList<Client>> =
        MutableStateFlow(DataViewModel.cliente.value.toMutableList())
    val client: StateFlow<MutableList<Client>> = _client.asStateFlow()

    // Lista de empleados
    private val _employee: MutableStateFlow<MutableList<Employee>> =
        MutableStateFlow(DataViewModel.employees.value.toMutableList())
    val employee: StateFlow<MutableList<Employee>> = _employee.asStateFlow()

    // Lista de managers
    private val _manager: MutableStateFlow<MutableList<Manager>> =
        MutableStateFlow(DataViewModel.manager.value.toMutableList())
    val manager: StateFlow<MutableList<Manager>> = _manager.asStateFlow()

    // Lista de proyectos
    private val _project: MutableStateFlow<MutableList<Project>> =
        MutableStateFlow(DataViewModel.projects.value.toMutableList())
    val project: StateFlow<MutableList<Project>> = _project.asStateFlow()

    // Lista de roles
    private val _rol: MutableStateFlow<MutableList<Rol>> =
        MutableStateFlow(DataViewModel.roles.value.toMutableList())
    val rol: StateFlow<MutableList<Rol>> = _rol.asStateFlow()

    // Lista de códigos de tiempo
    private val _timeCode: MutableStateFlow<MutableList<TimeCodeDTO>> =
        MutableStateFlow(DataViewModel.timeCodes.value.toMutableList())
    val timeCode: StateFlow<MutableList<TimeCodeDTO>> = _timeCode.asStateFlow()

    // Lista de órdenes de trabajo
    private val _workOrder: MutableStateFlow<MutableList<WorkOrder>> =
        MutableStateFlow(DataViewModel.workOrders.value.toMutableList())
    val workOrder: StateFlow<MutableList<WorkOrder>> = _workOrder.asStateFlow()
}
