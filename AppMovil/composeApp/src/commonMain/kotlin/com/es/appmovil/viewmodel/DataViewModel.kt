package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import com.es.appmovil.database.Database
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Aircraft
import com.es.appmovil.model.Area
import com.es.appmovil.model.Calendar
import com.es.appmovil.model.Client
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.EmployeeWO
import com.es.appmovil.model.EmployeeWorkHours
import com.es.appmovil.model.Manager
import com.es.appmovil.model.Project
import com.es.appmovil.model.ProjectTimeCode
import com.es.appmovil.model.Rol
import com.es.appmovil.model.TimeCode
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


/**
 * Objeto singleton que maneja la carga, almacenamiento y exposición reactiva
 * de los datos principales de la app, provenientes de la base de datos.
 *
 * Utiliza 'MutableStateFlow' para el estado mutable interno y
 * expone solo 'StateFlow' para lectura externa.
 *
 * Realiza carga inicial en 'init' mediante corrutinas en [Dispatchers.IO].
 */
object DataViewModel {

    /** Fecha actual del sistema, actualizable y observable. */
     var today =
            MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)

    /** Fecha actual usada en pantallas o filtros (puede diferir de [today]). */
    var currentToday =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)

    /** Empleado actualmente autenticado / activo. */
    var employee = Employee(-1, "", "", "", "", null, -1, null, "", "", null)

    /** Datos anuales del usuario (uso personalizado). */
    var employeesYearData = MutableStateFlow<MutableList<UserYearData>>(mutableListOf())

    /** Email del usuario actual, obtenido desde la configuración. */
    private var _currentEmail = MutableStateFlow("")
    val currentEmail: StateFlow<String> = _currentEmail

    // Variables comunes a varias pantallas

    /** Horas totales actuales registradas para el empleado. */
    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    /** Mes actual en formato string ("1".."12"). */
    private var _currentMonth = MutableStateFlow("0")

    /** Año actual en formato string. */
    var _currentYear = MutableStateFlow("0")

    /** Horas diarias predeterminadas para cálculo y UI. */
    private var _dailyHours = MutableStateFlow(8)
    val dailyHours: StateFlow<Int> = _dailyHours

    /** Lista mutable para gráfico tipo Pie (composición de actividades). */
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

    /**
     * Inicializa la carga de todos los datos desde la base,
     * cada uno en una corrutina con Dispatcher.IO para no bloquear UI.
     */
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
        cargarEmployeeWH()
    }

    /**
     * Obtiene y actualiza el email actual desde la base de configuración.
     * @return email recuperado o cadena vacía si no existe.
     */
    suspend fun cargarYObtenerEmail(): String {
        val datos = Database.getConfigData("email")
        if (datos != null) {
            _currentEmail.value = datos.valor
            return datos.valor
        }
        return ""
    }

    // Métodos privados para carga de datos individuales

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

    private fun cargarEmployees() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Employee>("Employee")
            employees.value = datos.toMutableList()
        }
    }

    private val _roles = MutableStateFlow<List<Rol>>(emptyList())
    val roles: StateFlow<List<Rol>> = _roles

    private fun cargarRoles() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Rol>("Rol")
            _roles.value = datos
        }
    }

    private val _aircraft = MutableStateFlow<List<Aircraft>>(emptyList())
    val aircraft: StateFlow<List<Aircraft>> = _aircraft

    private fun cargarAircraft() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Aircraft>("Aircraft")
            _aircraft.value = datos
        }
    }

    fun load_tables() {
        cargarActivities()
        cargarAircraft()
        cargarArea()
        cargarCalendar()
        cargarCliente()
        cargarEmployees()
        cargarManager()
        cargarProjects()
        cargarRoles()
        cargarTimeCodes()
        cargarWorkOrders()
        cargarTablesNames()
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

    private val _cliente = MutableStateFlow<List<Client>>(emptyList())
    val cliente: StateFlow<List<Client>> = _cliente

    private fun cargarCliente() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Client>("Client")
            _cliente.value = datos
        }
    }


    private val _employeeWH = MutableStateFlow<List<EmployeeWorkHours>>(emptyList())
    //val employeeWH: StateFlow<List<EmployeeWorkHours>> = _employeeWH

    private fun cargarEmployeeWH() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<EmployeeWorkHours>("EmployeeWorkHours")
            _employeeWH.value = datos
        }
    }

    private val _manager = MutableStateFlow<List<Manager>>(emptyList())
    val manager: StateFlow<List<Manager>> = _manager

    private fun cargarManager() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<Manager>("Manager")
            _manager.value = datos
        }
    }

    private val _tablesNames = MutableStateFlow<List<String>>(emptyList())
    val tablesNames: StateFlow<List<String>> = _tablesNames

    private fun cargarTablesNames() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getTablesNames()
            _tablesNames.value = datos
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

    /**
     * Carga datos anuales del usuario desde base y actualiza la lista.
     */
    fun cargarUserYearData() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = Database.getData<UserYearData>("UserYearData")
            employeesYearData.value = datos.toMutableList()
        }
    }

    /**
     * Actualiza [calendarFest] con las fechas festivas del calendario actual.
     */
    fun cargarCalendarFest() {
        val festivos = calendar.value.map { it.date }

        _calendarFest.value = CalendarYearDTO(
            idCalendar = today.value.year,
            date = festivos
        )
    }

    /**
     * Calcula y actualiza las horas totales registradas por el empleado actual.
     */
    fun getHours() {
        _currentHours.value = 0
        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                _currentHours.value += it.time.toInt()
            }
    }

    /**
     * Actualiza los valores de mes y año actuales según la fecha [today].
     */
    fun getMonth() {
        _currentMonth.value = today.value.monthNumber.toString()
        _currentYear.value = today.value.year.toString()
    }

    /**
     * Resetea la fecha `today` al día actual del sistema
     * y actualiza el mes y año activos mediante [changeMonth].
     */
    fun resetToday() {
        today.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
    }

    /**
     * Cambia el mes actual usado en filtros y recarga las actividades.
     * @param month nuevo mes en formato string.
     */
    fun changeMonth(month: String, year: String) {
        _currentMonth.value = month
        _currentYear.value = year
    }

    /**
     * Construye y actualiza la lista para el gráfico Pie con
     * colores y horas por cada código de tiempo.
     */
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

    /**
     * Actualiza la lista de gráficos de pastel (`pies`) agregando o sumando
     * el tiempo de la actividad pasada según su TimeCode.
     *
     * @param pies Lista mutable de objetos [Pie] que representan segmentos del gráfico.
     * @param activity Actividad del empleado cuya duración se añadirá al gráfico.
     */
    private fun createPie(pies: MutableList<Pie>, activity: EmployeeActivity) {
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