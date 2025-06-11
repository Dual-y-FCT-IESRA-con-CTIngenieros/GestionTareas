package com.es.appmovil.viewmodel

import androidx.lifecycle.ViewModel
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Aircraft
import com.es.appmovil.model.Area
import com.es.appmovil.model.Calendar
import com.es.appmovil.model.Client
import com.es.appmovil.model.Employee
import com.es.appmovil.model.Manager
import com.es.appmovil.model.Project
import com.es.appmovil.model.Rol
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.model.dto.TimeCodeDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TableManageViewModel : ViewModel() {

    private val _activities: MutableStateFlow<MutableList<Activity>> =
        MutableStateFlow(DataViewModel.activities.value.toMutableList())
    val activities: StateFlow<MutableList<Activity>> = _activities.asStateFlow()

    private val _aircraft: MutableStateFlow<MutableList<Aircraft>> =
        MutableStateFlow(DataViewModel.aircraft.value.toMutableList())
    val aircraft: StateFlow<MutableList<Aircraft>> = _aircraft.asStateFlow()

    private val _area: MutableStateFlow<MutableList<Area>> =
        MutableStateFlow(DataViewModel.areas.value.toMutableList())
    val area: StateFlow<MutableList<Area>> = _area.asStateFlow()

    private val _calendar: MutableStateFlow<MutableList<Calendar>> =
        MutableStateFlow(DataViewModel.calendar.value.toMutableList())
    val calendar: StateFlow<MutableList<Calendar>> = _calendar.asStateFlow()

    private val _client: MutableStateFlow<MutableList<Client>> =
        MutableStateFlow(DataViewModel.cliente.value.toMutableList())
    val client: StateFlow<MutableList<Client>> = _client.asStateFlow()

    private val _employee: MutableStateFlow<MutableList<Employee>> =
        MutableStateFlow(DataViewModel.employees.value.toMutableList())
    val employee: StateFlow<MutableList<Employee>> = _employee.asStateFlow()

    private val _manager: MutableStateFlow<MutableList<Manager>> =
        MutableStateFlow(DataViewModel.manager.value.toMutableList())
    val manager: StateFlow<MutableList<Manager>> = _manager.asStateFlow()

    private val _project: MutableStateFlow<MutableList<Project>> =
        MutableStateFlow(DataViewModel.projects.value.toMutableList())
    val project: StateFlow<MutableList<Project>> = _project.asStateFlow()

    private val _rol: MutableStateFlow<MutableList<Rol>> =
        MutableStateFlow(DataViewModel.roles.value.toMutableList())
    val rol: StateFlow<MutableList<Rol>> = _rol.asStateFlow()

    private val _timeCode: MutableStateFlow<MutableList<TimeCodeDTO>> =
        MutableStateFlow(DataViewModel.timeCodes.value.toMutableList())
    val timeCode: StateFlow<MutableList<TimeCodeDTO>> = _timeCode.asStateFlow()

    private val _workOrder: MutableStateFlow<MutableList<WorkOrder>> =
        MutableStateFlow(DataViewModel.workOrders.value.toMutableList())
    val workOrder: StateFlow<MutableList<WorkOrder>> = _workOrder.asStateFlow()


}