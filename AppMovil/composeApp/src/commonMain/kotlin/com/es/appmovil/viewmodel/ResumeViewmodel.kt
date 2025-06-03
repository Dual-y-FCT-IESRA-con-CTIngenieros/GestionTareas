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

class ResumeViewmodel {

    private val employeeActivities = MutableStateFlow(DataViewModel.employeeActivities.value)
    private val employee = DataViewModel.employee
    private val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    private var _currentDay = MutableStateFlow(getDays())
    val currentDay: StateFlow<Int> = _currentDay

    private var _timeActivity = MutableStateFlow(mutableMapOf<Long, Float>())
    val timeActivity: StateFlow<MutableMap<Long, Float>> = _timeActivity

    fun getWeekDaysWithNeighbors(year: Int, month: Int, day: Int): List<LocalDate> {
        val selectedDate = LocalDate(year, month, day)
        val dayOfWeek = selectedDate.dayOfWeek.isoDayNumber // 1 (Lunes) a 7 (Domingo)

        val firstDayOfWeek = selectedDate.minus(dayOfWeek - 1, DateTimeUnit.DAY)

        return (0..6).map { firstDayOfWeek.plus(it, DateTimeUnit.DAY) }
    }

    fun onMonthChangePrevious(period: DatePeriod) {
        today.value = today.value.minus(period)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
        getTimeActivity()
    }

    fun onWeekChangeFordward(period: DatePeriod) {
        today.value = today.value.plus(period)
        changeMonth(today.value.monthNumber.toString(), today.value.year.toString())
        getPie()
        getTimeActivity()
    }

    private fun getDays(): Int {
        val year = today.value.year

        val holidays = calendarFest.value.date.map { LocalDate.parse(it) }.toSet()

        var workingDays = 0

        for (date in generateSequence(LocalDate(year, 1, 1)) { it.plus(DatePeriod(days = 1)) }
            .takeWhile { it <= today.value }) {

            val isWeekend =
                date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
            val isHoliday = holidays.contains(date)

            if (!isWeekend && !isHoliday) {
                workingDays++
            }
        }

        return workingDays
    }

    fun getLegend(): MutableState<MutableMap<String, Long>> {
        val legend = mutableStateOf(mutableMapOf<String, Long>())

        timeCodes.value.forEach {
            legend.value[it.desc.take(10)] = it.color
        }
        return legend
    }

    fun getTimeActivity() {
        val updatedMap = mutableMapOf<Long, Float>()

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee && it.date.split("-")[0] == _currentYear.value }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                if (timeCode != null) {
                    if (updatedMap.containsKey(timeCode.color)) {
                        val hours = updatedMap[timeCode.color]?.plus(it.time)
                        updatedMap[timeCode.color] = hours ?: 0f
                    } else {
                        updatedMap[timeCode.color] = it.time
                    }
                }
            }
        _timeActivity.value = updatedMap
    }

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
