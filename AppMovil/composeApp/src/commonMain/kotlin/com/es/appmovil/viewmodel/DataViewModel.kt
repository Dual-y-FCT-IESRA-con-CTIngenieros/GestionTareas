package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import com.es.appmovil.database.Database
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.EmployeeWO
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.Project
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.utils.DTOConverter.toDTO
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object DataViewModel {

    private val _timeCodes = MutableStateFlow<List<TimeCodeDTO>>(emptyList())
    val timeCodes: StateFlow<List<TimeCodeDTO>> = _timeCodes

    private fun cargarTimeCodes() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<TimeCode>("TimeCode")
            _timeCodes.value = datos.map { it.toDTO() }

        }
    }

    val _employeeActivities = MutableStateFlow<MutableList<EmployeeActivity>>(mutableListOf())
    val employeeActivities: StateFlow<MutableList<EmployeeActivity>> = _employeeActivities

    private fun cargarEmployeeActivities() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeActivity>("EmployeeActivity")
            _employeeActivities.value = datos.toMutableList()
        }
    }

    val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private fun cargarProjects() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Project>("Project")
            _projects.value = datos
        }
    }

    init {
        cargarTimeCodes()
        cargarEmployeeActivities()
        cargarProjects()
    }

    val employee = Employee(1, "Antonio", "Pardeza Julia", "apardeza@ctengineeringgroup.com", "2012-01-05", null, 1)

    val proyectTimecodes = listOf(
        ProjectTimeCode("K2312273", 100),
        ProjectTimeCode("K2312273", 555),
        ProjectTimeCode("K2315252", 100),
        ProjectTimeCode("M1638790", 900),
    )

    val workOrders = listOf(
        WorkOrder("K2312273-1", "hola", 1, "K2312273", 1),
        WorkOrder("K2315252-1", "hola", 1, "K2315252", 1),
        WorkOrder("M1638790-1", "hola", 1, "M1638790", 1)
    )

    val employeeWO = listOf(
        EmployeeWO("K2312273-1",1, "hola", "2025-02-06", "2025-05-06"),
        EmployeeWO("K2315252-1",1, "hola", "2025-02-06", "2025-05-06"),
        EmployeeWO("M1638790-1",1, "hola", "2025-02-06", "2025-05-06")
    )

    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    private var _pieList = MutableStateFlow(mutableListOf<Pie>())
    val pieList:StateFlow<MutableList<Pie>> = _pieList

    fun getHours() {
        _currentHours.value = 0
        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                _currentHours.value += it.time.toInt()
            }
    }

    fun getPie() {
        val pies = mutableListOf<Pie>()

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                if (timeCode != null){
                    val pie = pies.find { p -> p.label == timeCode.idTimeCode.toString() }

                    if (pie != null) {
                        val timePie = pie.data + it.time
                        pies.remove(pie)
                        pies.add(
                            Pie(
                                label = timeCode.desc,
                                data = timePie,
                                color = Color(timeCode.color.toLong())
                            )
                        )
                    } else {
                        pies.add(
                            Pie(
                                label = timeCode.desc,
                                data = it.time.toDouble(),
                                color = Color(timeCode.color.toLong())
                            )
                        )
                    }
                }
            }
        _pieList.value = pies
    }


}