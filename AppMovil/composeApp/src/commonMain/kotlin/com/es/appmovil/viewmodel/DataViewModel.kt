package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import com.es.appmovil.database.Database
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Aircraft
import com.es.appmovil.model.Area
import com.es.appmovil.model.Calendar
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.EmployeeWO
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.Project
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.Rol
import com.es.appmovil.model.UserYearData
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.model.dto.CalendarYearDTO
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.utils.DTOConverter.toDTO
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DataViewModel {

    var today = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    var currentToday = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)

    var employee = Employee(-1, "", "", "", "", null, -1,null, "", "", null)

    var employeesYearData = MutableStateFlow<MutableList<UserYearData>>(mutableListOf())

    // Variables comunes a varias pantallas
    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    private var _currentMonth = MutableStateFlow("0")
    var _currentYear = MutableStateFlow("0")

    private var _dailyHours = MutableStateFlow(8)
    val dailyHours: StateFlow<Int> = _dailyHours

    private var _pieList = MutableStateFlow(mutableListOf<Pie>())
    val pieList: StateFlow<MutableList<Pie>> = _pieList


    // Listas con todos los datos de las tablas
    private val _timeCodes = MutableStateFlow<List<TimeCodeDTO>>(emptyList())
    val timeCodes: StateFlow<List<TimeCodeDTO>> = _timeCodes

    private val _employeeActivities =
        MutableStateFlow<MutableList<EmployeeActivity>>(mutableListOf())
    val employeeActivities: StateFlow<MutableList<EmployeeActivity>> = _employeeActivities

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private val _projectTimeCodes = MutableStateFlow<List<ProjectTimeCode>>(emptyList())
    val projectTimeCodes: StateFlow<List<ProjectTimeCode>> = _projectTimeCodes

    private val _workOrders = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    private val _employeeWO = MutableStateFlow<List<EmployeeWO>>(emptyList())
    val employeeWO: StateFlow<List<EmployeeWO>> = _employeeWO

    private val _aircrafts = MutableStateFlow<List<Aircraft>>(emptyList())
    val aircrafts: StateFlow<List<Aircraft>> = _aircrafts

    // Carga de los datos de la base de datos
    init {
        cargarTimeCodes()
        cargarEmployeeActivities()
        cargarProjects()
        cargarProjectsTimeCode()
        cargarWorkOrders()
        cargarActivities()
        cargarEmployeeWO()
        cargarEmployees()
        cargarRoles()
        cargarCalendar()
        cargarUserYearData()
        cargarArea()
        cargarAircraft()
    }

    private fun cargarTimeCodes() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<TimeCode>("TimeCode")
            _timeCodes.value = datos.map { it.toDTO() }

        }
    }

    private fun cargarEmployeeActivities() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeActivity>("EmployeeActivity")
            _employeeActivities.value = datos.toMutableList()
        }
    }

    private fun cargarProjects() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Project>("Project")
            _projects.value = datos
        }
    }

    private fun cargarProjectsTimeCode() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<ProjectTimeCode>("ProjectTimeCode")
            _projectTimeCodes.value = datos
        }
    }
    private fun cargarWorkOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<WorkOrder>("WorkOrder")
            _workOrders.value = datos
        }
    }

    private fun cargarActivities() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Activity>("Activity")
            _activities.value = datos

        }
    }

    private fun cargarEmployeeWO() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeWO>("EmployeeWO")
            _employeeWO.value = datos
        }
    }

    val employees = MutableStateFlow<MutableList<Employee>>(mutableListOf())

    private fun cargarEmployees(){
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Employee>("Employee")
            employees.value = datos.toMutableList()
        }
    }

    private val _roles = MutableStateFlow<List<Rol>>(emptyList())
    val roles: StateFlow<List<Rol>> = _roles

    fun cargarRoles() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Rol>("Rol")
            _roles.value = datos
        }
    }

    private var _calendarFest = MutableStateFlow(CalendarYearDTO(0, mutableListOf()))
    val calendarFest = _calendarFest

    private var _calendar = MutableStateFlow<MutableList<Calendar>>(mutableListOf())
    val calendar = _calendar

    private fun cargarCalendar() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Calendar>("Calendar")
            _calendar.value = datos.toMutableList()
        }
    }

    private var _areas = MutableStateFlow<List<Area>>(emptyList())
    val areas = _areas


    private fun cargarArea() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Area>("Area")
            _areas.value = datos
        }
    }

    fun cargarUserYearData() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<UserYearData>("UserYearData")
            employeesYearData.value = datos.toMutableList()
        }
    }


    fun cargarCalendarFest() {
        val festivos = calendar.value.map { it.date }

        _calendarFest.value = CalendarYearDTO(
            idCalendar = today.value.year,
            date = festivos
        )
    }

    fun cargarAircraft() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Aircraft>("Aircraft")
            _aircrafts.value = datos.toMutableList()
        }
    }

    // Funciones comunes a varias pantallas
    fun getHours() {
        _currentHours.value = 0
        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                _currentHours.value += it.time.toInt()
            }
    }

    fun getMonth() {
        _currentMonth.value = today.value.monthNumber.toString()
        _currentYear.value = today.value.year.toString()
    }

    fun resetToday() {
        today.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
    }

    fun changeMonth(month: String, year: String) {
        _currentMonth.value = month
        _currentYear.value = year
    }

    fun getPie() {
        val pies = mutableListOf<Pie>()
        val dateFilter =
            if (today.value.monthNumber.toString().length == 1) "0${_currentMonth.value}"
            else _currentMonth.value
        employeeActivities.value
            .filter {
                employee.idEmployee == it.idEmployee
                        && it.date.split("-")[1] == dateFilter
                        && it.date.split("-")[0] == _currentYear.value
            }
            .forEach { activity ->
                createPie(pies, activity)
            }
        _pieList.value = pies
    }

    private fun createPie(pies:MutableList<Pie>, activity:EmployeeActivity) {
        val timeCode = timeCodes.value.find { time -> time.idTimeCode == activity.idTimeCode }
        if (timeCode != null) {
            val pie = pies.find { p -> p.label == timeCode.idTimeCode.toString() }

            if (pie != null) {
                val timePie = pie.data + activity.time
                pies.remove(pie)
                pies.add(
                    Pie(
                        label = timeCode.idTimeCode.toString(),
                        data = timePie,
                        color = Color(timeCode.color)
                    )
                )
            } else {
                pies.add(
                    Pie(
                        label = timeCode.idTimeCode.toString(),
                        data = activity.time.toDouble(),
                        color = Color(timeCode.color)
                    )
                )
            }
        }
    }
}