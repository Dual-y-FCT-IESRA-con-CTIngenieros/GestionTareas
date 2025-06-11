package com.es.appmovil.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel._currentYear
import com.es.appmovil.viewmodel.DataViewModel.calendarFest
import com.es.appmovil.viewmodel.DataViewModel.changeMonth
import com.es.appmovil.viewmodel.DataViewModel.getPie
import com.es.appmovil.viewmodel.DataViewModel.today
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * ViewModel para la pantalla de resumen de actividad del empleado.
 * Gestiona:
 * - Días trabajados
 * - Actividades semanales
 * - Actividades por código de tiempo
 * - Leyenda de colores
 */
class ResumeViewmodel {

    // Flujo de actividades del empleado
    private val employeeActivities = MutableStateFlow(DataViewModel.employeeActivities.value)
    private val employee = DataViewModel.employee
    private val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    // Días laborales acumulados hasta la fecha actual
    private var _currentDay = MutableStateFlow(getDays())
    val currentDay: StateFlow<Int> = _currentDay

    // Mapa de tiempo total por color (para el pie chart)
    private var _timeActivity = MutableStateFlow(mutableMapOf<Long, Float>())
    val timeActivity: StateFlow<MutableMap<Long, Float>> = _timeActivity

    /**
     * Devuelve una lista de 7 días alrededor de la fecha seleccionada (semana completa)
     */
    fun getWeekDaysWithNeighbors(year: Int, month: Int, day: Int): List<LocalDate> {
        val selectedDate = LocalDate(year, month, day)
        val dayOfWeek = selectedDate.dayOfWeek.isoDayNumber // 1=Lunes ... 7=Domingo
        val firstDayOfWeek = selectedDate.minus(dayOfWeek - 1, DateTimeUnit.DAY)
        return (0..6).map { firstDayOfWeek.plus(it, DateTimeUnit.DAY) }
    }

    /**
     * Retrocede un mes completo y actualiza los datos
     */
    fun onMonthChangePrevious(period: DatePeriod) {
        today.value = today.value.minus(period)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie() // actualizar gráfica de sectores
        getTimeActivity()
    }

    /**
     * Avanza un mes completo y actualiza los datos
     */
    fun onWeekChangeFordward(period: DatePeriod) {
        today.value = today.value.plus(period)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
        getTimeActivity()
    }

    /**
     * Calcula el número de días laborables desde inicio de año hasta hoy
     */
    private fun getDays(): Int {
        val year = today.value.year
        val holidays = calendarFest.value.date.map { LocalDate.parse(it) }.toSet()

        var workingDays = 0
        for (date in generateSequence(LocalDate(year, 1, 1)) { it.plus(DatePeriod(days = 1)) }
            .takeWhile { it <= today.value }) {

            val isWeekend = date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
            val isHoliday = holidays.contains(date)

            if (!isWeekend && !isHoliday) {
                workingDays++
            }
        }
        return workingDays
    }

    /**
     * Devuelve la leyenda de colores (descripcion - color) para los gráficos
     */
    fun getLegend(): MutableState<MutableMap<String, Long>> {
        val legend = mutableStateOf(mutableMapOf<String, Long>())
        timeCodes.value.forEach {
            legend.value[it.desc.take(10)] = it.color
        }
        return legend
    }

    /**
     * Calcula el total de horas por código de tiempo para el gráfico de sectores.
     */
    fun getTimeActivity() {
        val updatedMap = mutableMapOf<Long, Float>()

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee && it.date.split("-")[0] == _currentYear.value }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                if (timeCode != null) {
                    updatedMap[timeCode.color] = (updatedMap[timeCode.color] ?: 0f) + it.time
                }
            }
        _timeActivity.value = updatedMap
    }

    /**
     * Calcula la actividad diaria por colores para el calendario.
     */
    fun getDayActivity(): MutableState<MutableMap<String, MutableList<Color>>> {
        val dayActivity = mutableStateOf(mutableMapOf<String, MutableList<Color>>())

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                timeCode?.let { tc ->
                    val currentList = dayActivity.value[it.date] ?: mutableListOf()
                    currentList.add(Color(tc.color))
                    dayActivity.value[it.date] = currentList
                }
            }
        return dayActivity
    }
}
