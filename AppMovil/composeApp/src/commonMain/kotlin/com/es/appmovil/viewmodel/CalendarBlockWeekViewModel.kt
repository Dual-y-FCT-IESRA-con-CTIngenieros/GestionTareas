package com.es.appmovil.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.es.appmovil.database.Database
import com.es.appmovil.model.Employee
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.today
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * ViewModel encargado de gestionar el estado del calendario semanal,
 * permitiendo bloquear y desbloquear semanas para empleados.
 */
class CalendarBlockWeekViewModel {

    // Estado que indica si se debe mostrar el diálogo de bloqueo/desbloqueo
    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    // Estado que indica si se ha modificado algún empleado
    private var _employeeModifie = MutableStateFlow(false)
    val employeeModifie: StateFlow<Boolean> = _employeeModifie

    // Índice de la semana seleccionada
    private var _weekIndex = MutableStateFlow(0)
    val weekIndex: StateFlow<Int> = _weekIndex

    // Última fecha de bloqueo registrada
    private var _blockDate: MutableStateFlow<String?> = MutableStateFlow("")
    val blockDate: StateFlow<String?> = _blockDate

    // Texto de filtro actual
    private var _filter: MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    // Lista de semanas en el mes actual
    private val _weeksInMonth =
        MutableStateFlow(generateWeeksForMonth(today.value.year, today.value.month))
    val weeksInMonth: StateFlow<List<Pair<LocalDate, LocalDate>>> = _weeksInMonth

    // Lista de semanas bloqueadas
    private val _locked = MutableStateFlow<List<Pair<LocalDate, LocalDate>>>(emptyList())
    val locked: StateFlow<List<Pair<LocalDate, LocalDate>>> = _locked

    /** Cambia el estado del diálogo */
    fun changeDialog(bool: Boolean) {
        _showDialog.value = bool
    }

    /** Cambia el índice de la semana seleccionada */
    fun changeWeekIndex(index: Int) {
        _weekIndex.value = index
    }

    /** Cambia el estado de modificación de empleados */
    fun changeEmployeesModifie(bool: Boolean) {
        _employeeModifie.value = bool
    }

    /** Cambia el filtro de búsqueda */
    fun changeFilter(value: String) {
        _filter.value = value
    }

    /** Reinicia las semanas mostradas en base al mes actual */
    fun resetWeeks() {
        _weeksInMonth.value = generateWeeksForMonth(today.value.year, today.value.month)
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia atras
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        today.value = today.value.minus(month)
        _weeksInMonth.value = generateWeeksForMonth(today.value.year, today.value.month)
        changeEmployeesModifie(false)
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia delante
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        today.value = today.value.plus(month)
        _weeksInMonth.value = generateWeeksForMonth(today.value.year, today.value.month)
        changeEmployeesModifie(false)
    }

    /**
     * Determina el color e ícono de una semana según el estado de bloqueo de los empleados.
     * @param week Semana a evaluar
     * @param employees Lista de empleados
     * @return Par (ícono, color) representativo del estado de la semana
     */
    fun getWeekColor(
        week: Pair<LocalDate, LocalDate>,
        employees: List<Employee>
    ): Pair<ImageVector, Color> {
        val weekEnd = week.second

        val states = employees.mapNotNull {
            it.blockDate?.let { dateStr ->
                runCatching { LocalDate.parse(dateStr) }.getOrNull()
            }?.let { blockDate ->
                blockDate >= weekEnd
            }
        }

        val unlockMatch = employees.any { employee ->
            employee.unblockDate?.let { unlockRangeStr ->
                val parts = unlockRangeStr.split("/")
                if (parts.size == 2) {
                    val start = runCatching { LocalDate.parse(parts[0]) }.getOrNull()
                    val end = runCatching { LocalDate.parse(parts[1]) }.getOrNull()
                    if (start != null && end != null) {
                        weekEnd == start || weekEnd == end
                    } else false
                } else false
            } ?: false
        }

        val color: Color = when {
            unlockMatch -> Color(0xFFFFA500)  // color especial si coincide con unlockDate
            states.all { it } -> Color.Red
            states.none { it } -> Color.Green
            else -> Color.Red // naranja (parcialmente bloqueados)
        }

        val icon: ImageVector = if (color == Color.Red) Icons.Filled.Lock else Icons.Filled.LockOpen

        return icon to color
    }

    /**
     * Bloquea o desbloquea una semana específica para un empleado.
     * @param week Semana objetivo
     * @param employee Empleado a actualizar
     * @param unlock Si es `true`, se elimina el desbloqueo
     */
    fun lockWeekEmployee(week: Pair<LocalDate, LocalDate>, employee: Employee, unlock: Boolean) {
        val updated =
            if (unlock) employee.copy(unblockDate = null) else employee.copy(unblockDate = "${week.first}/${week.second}")
        employees.update { list ->
            list.map { if (it.idEmployee == updated.idEmployee) updated else it }.toMutableList()
        }
    }

    /**
     * Bloquea la semana completa para todos los empleados y actualiza la base de datos.
     * @param week Semana que se quiere bloquear
     */
    fun lockWeek(week: Pair<LocalDate, LocalDate>) {
        employees.value.forEach {
            it.blockDate = week.second.toString()
            CoroutineScope(Dispatchers.IO).launch {
                Database.updateEmployee(
                    EmployeeUpdateDTO(
                        it.idEmployee,
                        it.nombre,
                        it.apellidos,
                        it.email,
                        it.dateFrom,
                        it.dateTo,
                        it.idRol,
                        it.blockDate,
                        it.idCT,
                        it.idAirbus,
                        it.unblockDate
                    )
                )
            }
        }
        generateLock()
    }


    /**
     * Guarda los cambios de bloqueo actual de todos los empleados en la base de datos.
     */
    fun lockWeekEmployees() {
        employees.value.forEach {
            CoroutineScope(Dispatchers.IO).launch {
                Database.updateEmployee(
                    EmployeeUpdateDTO(
                        it.idEmployee,
                        it.nombre,
                        it.apellidos,
                        it.email,
                        it.dateFrom,
                        it.dateTo,
                        it.idRol,
                        it.blockDate,
                        it.idCT,
                        it.idAirbus,
                        it.unblockDate
                    )
                )
            }
        }
        generateLock()
    }

    /**
     * Devuelve la semana anterior a la dada.
     * @param week Semana actual
     * @return Semana previa como par de fechas
     */
    fun getPreviousWeek(week: Pair<LocalDate, LocalDate>): Pair<LocalDate, LocalDate> {
        val previousWeekEnd =
            week.first.minus(DatePeriod(days = 1)) // día anterior al primer día de la semana actual
        val previousWeekStart = previousWeekEnd.minus(DatePeriod(days = 6))
        return previousWeekStart to previousWeekEnd
    }

    /**
     * Actualiza el estado `_blockDate` con la última fecha de bloqueo entre todos los empleados.
     */
    fun generateLock() {
        val e = employees.value.maxBy { it.blockDate ?: "" }
        _blockDate.value = e.blockDate
    }

    /**
     * Genera una lista de semanas completas para un mes específico.
     * Cada semana es un par de fechas: (lunes, domingo).
     * @param year Año deseado
     * @param month Mes deseado
     * @return Lista de semanas del mes
     */
    private fun generateWeeksForMonth(year: Int, month: Month): List<Pair<LocalDate, LocalDate>> {
        val firstDayOfMonth = LocalDate(year, month, 1)
        val lastDayOfMonth = LocalDate(year, month, getDaysInMonth(year, month))

        // Buscar el lunes anterior (o el mismo día si ya es lunes)
        var current = firstDayOfMonth
        while (current.dayOfWeek != DayOfWeek.MONDAY) {
            current = current.minus(DatePeriod(days = 1))
        }

        val weeks = mutableListOf<Pair<LocalDate, LocalDate>>()

        while (current <= lastDayOfMonth) {
            val startOfWeek = current
            val endOfWeek = current.plus(DatePeriod(days = 6))

            // Solo agregamos si algún día de la semana está dentro del mes
            if (startOfWeek <= lastDayOfMonth && endOfWeek >= firstDayOfMonth) {
                weeks.add(startOfWeek to endOfWeek)
            }

            // Avanzamos a la siguiente semana
            current = current.plus(DatePeriod(days = 7))
        }

        return weeks
    }

    /**
     * Obtiene el número de días de un mes específico.
     * @param year Año
     * @param month Mes
     * @return Número de días en el mes
     */
    private fun getDaysInMonth(year: Int, month: Month): Int {
        return when (month) {
            Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
            Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31

            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30

            Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            else -> -1
        }
    }

    /**
     * Determina si un año es bisiesto.
     *
     * @param year Año a comprobar.
     * @return `true` si es bisiesto, `false` en caso contrario.
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}