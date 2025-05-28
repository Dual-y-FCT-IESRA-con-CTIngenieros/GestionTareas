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

class CalendarManageViewModel {

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _employeeModifie = MutableStateFlow(false)
    val employeeModifie: StateFlow<Boolean> = _employeeModifie

    private var _weekIndex = MutableStateFlow(0)
    val weekIndex: StateFlow<Int> = _weekIndex

    private var _blockDate:MutableStateFlow<String?> = MutableStateFlow("")
    val blockDate: StateFlow<String?> = _blockDate

    private var _filter:MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _weeksInMonth = MutableStateFlow(generateWeeksForMonth(today.value.year, today.value.month))
    val weeksInMonth:StateFlow<List<Pair<LocalDate, LocalDate>>> = _weeksInMonth

    private val _locked = MutableStateFlow<List<Pair<LocalDate, LocalDate>>>(emptyList())
    val locked: StateFlow<List<Pair<LocalDate, LocalDate>>> = _locked

    fun changeDialog(bool:Boolean) {
        _showDialog.value = bool
    }

    fun changeWeekIndex(index:Int) {
        _weekIndex.value = index
    }

    fun changeEmployeesModifie(bool:Boolean) {
        _employeeModifie.value = bool
    }

    fun changeFilter(value:String) {
        _filter.value = value
    }

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

    fun getWeekColor(week: Pair<LocalDate, LocalDate>, employees: List<Employee>): Pair<ImageVector, Color> {
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

    fun lockWeekEmployee(week: Pair<LocalDate, LocalDate>, employee: Employee, unlock:Boolean) {
        val updated = if(unlock) employee.copy(unblockDate = null) else employee.copy(unblockDate = "${week.first}/${week.second}")
        employees.update { list ->
            list.map { if (it.idEmployee == updated.idEmployee) updated else it }.toMutableList()
        }
    }

    fun lockWeek(week: Pair<LocalDate, LocalDate>) {
        employees.value.forEach {
            it.blockDate = week.second.toString()
            CoroutineScope(Dispatchers.IO).launch {
                Database.updateEmployee(EmployeeUpdateDTO(it.idEmployee, it.nombre, it.apellidos, it.email, it.dateFrom, it.dateTo, it.idRol, it.blockDate, it.idCT, it.idAirbus, it.unblockDate))
            }
        }
        generateLock()
    }

    fun lockWeekEmployees() {
        employees.value.forEach {
            CoroutineScope(Dispatchers.IO).launch {
                Database.updateEmployee(EmployeeUpdateDTO(it.idEmployee, it.nombre, it.apellidos, it.email, it.dateFrom, it.dateTo, it.idRol, it.blockDate, it.idCT, it.idAirbus, it.unblockDate))
            }
        }
        generateLock()
    }

    fun getPreviousWeek(week: Pair<LocalDate, LocalDate>): Pair<LocalDate, LocalDate> {
        val previousWeekEnd = week.first.minus(DatePeriod(days = 1)) // día anterior al primer día de la semana actual
        val previousWeekStart = previousWeekEnd.minus(DatePeriod(days = 6))
        return previousWeekStart to previousWeekEnd
    }

    fun generateLock() {
        val e = employees.value.maxBy { it.blockDate ?:"" }
        _blockDate.value = e.blockDate
    }

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

    private fun getDaysInMonth(year: Int, month: Month): Int {
        return when (month) {
            Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
            Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31

            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30

            Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            else -> -1
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}