package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import com.es.appmovil.database.Database
import com.es.appmovil.database.Database.supabase
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.EmployeeWO
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.Project
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.utils.DTOConverter.toDTO
import io.github.jan.supabase.postgrest.from
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

object DataViewModel {

    private val _timeCodes = MutableStateFlow<List<TimeCodeDTO>>(emptyList())
    val timeCodes: StateFlow<List<TimeCodeDTO>> = _timeCodes

    private fun cargarTimeCodes() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<TimeCode>("TimeCode")
            _timeCodes.value = datos.map { it.toDTO() }

        }
    }

    private val _employeeActivities =
        MutableStateFlow<MutableList<EmployeeActivity>>(mutableListOf())
    val employeeActivities: StateFlow<MutableList<EmployeeActivity>> = _employeeActivities

    private fun cargarEmployeeActivities() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeActivity>("EmployeeActivity")
            _employeeActivities.value = datos.toMutableList()
        }
    }

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private fun cargarProjects() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Project>("Project")
            _projects.value = datos
        }
    }

    private val _projectTimeCodes = MutableStateFlow<List<ProjectTimeCode>>(emptyList())
    val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = _projectTimeCodes

    private fun cargarProjectsTimeCode() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<ProjectTimeCode>("ProjectTimeCode")
            _projectTimeCodes.value = datos
        }
    }

    private val _workOrders = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders

    private fun cargarWorkOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<WorkOrder>("WorkOrder")
            _workOrders.value = datos
        }
    }

    private val _employeeWO = MutableStateFlow<List<EmployeeWO>>(emptyList())
    val employeeWO: StateFlow<List<EmployeeWO>> = _employeeWO

    private fun cargarEmployeeWO() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeWO>("EmployeeWO")
            _employeeWO.value = datos
        }
    }

    fun uwu() {

        CoroutineScope(Dispatchers.IO).launch {
            val jsonData = supabase.from("Employee").select().data

            println("Raw json: $jsonData")

            val jsonArray = Json.parseToJsonElement(jsonData).jsonArray
            println("Json object: $jsonArray")

            val headers = jsonArray.first().jsonObject.keys
            println("Headers: $headers")

            var rows = ""

            for ((i, elemento) in jsonArray.withIndex()) {
                println("Ojeto $i:")
                val jsonObject = elemento.jsonObject
                for (clave in headers) {
                    rows += jsonObject[clave].toString() + ","
                    println("$clave: ${jsonObject[clave]}")
                }
                rows += "\n"
            }
//        val rows = dataTable.map { emp ->
//            listOf(
//                emp.nombre,
//                emp.apellidos,
//                emp.email,
//                emp.dateFrom,
//                emp.dateTo ?: "",
//                emp.idRol.toString(),
//                emp.idEmployee.toString()
//            ).joinToString(",")
//        }
            val csv = listOf(headers, rows)
            println(csv)
        }
    }

    init {
        cargarTimeCodes()
        cargarEmployeeActivities()
        cargarProjects()
        cargarProjectsTimeCode()
        cargarWorkOrders()
        cargarEmployeeWO()
        uwu()
    }

    val employee = Employee(
        1,
        "Antonio",
        "Pardeza Julia",
        "apardeza@ctengineeringgroup.com",
        "2012-01-05",
        null,
        1
    )


    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    private var _pieList = MutableStateFlow(mutableListOf<Pie>())
    val pieList: StateFlow<MutableList<Pie>> = _pieList

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
                if (timeCode != null) {
                    val pie = pies.find { p -> p.label == timeCode.idTimeCode.toString() }

                    if (pie != null) {
                        val timePie = pie.data + it.time
                        pies.remove(pie)
                        pies.add(
                            Pie(
                                label = timeCode.idTimeCode.toString(),
                                data = timePie,
                                color = Color(timeCode.color.toLong())
                            )
                        )
                    } else {
                        pies.add(
                            Pie(
                                label = timeCode.idTimeCode.toString(),
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