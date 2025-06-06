package com.es.appmovil.viewmodel

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
import kotlinx.coroutines.flow.StateFlow

class TableManageViewModel {

    private val activities: StateFlow<List<Activity>> = DataViewModel.activities
    private val aircraft: StateFlow<List<Aircraft>> = DataViewModel.aircraft
    private val area: StateFlow<List<Area>> = DataViewModel.area
    private val calendar: StateFlow<List<Calendar>> = DataViewModel.calendar
    private val client: StateFlow<List<Client>> = DataViewModel.cliente
    private val employee: StateFlow<List<Employee>> = DataViewModel.employees
    private val manager: StateFlow<List<Manager>> = DataViewModel.manager
    private val project: StateFlow<List<Project>> = DataViewModel.projects
    private val rol: StateFlow<List<Rol>> = DataViewModel.roles
    private val timeCode: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes
    private val workOrder: StateFlow<List<WorkOrder>> = DataViewModel.workOrders




}