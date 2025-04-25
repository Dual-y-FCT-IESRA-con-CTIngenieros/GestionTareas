package com.es.appmovil.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.TimeCode
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResumeViewmodel {
    
    private val employeeActivities = DataViewModel.employeeActivities
    private val employee = DataViewModel.employee
    private val timeCodes = DataViewModel.timeCodes

    private var _dailyHours = MutableStateFlow(8)
    val dailyHours: StateFlow<Int> = _dailyHours

    private var _currentDay = MutableStateFlow(getDays())
    val currentDay: StateFlow<Int> = _currentDay

    
    private fun getDays():Int {
        var fc = ""
        var days = 0
        employeeActivities
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                if (fc != it.date) {
                    days += 1
                    fc = it.date
                }
            }
        return days
    }

    fun getPie() : MutableState<MutableList<Pie>> {
        val pies = mutableStateOf(mutableListOf<Pie>())

        employeeActivities
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
            val timeCode = timeCodes.find { time -> time.idTimeCode == it.idTimeCode }
            if (timeCode != null){
                val pie = pies.value.find { p -> p.label == timeCode.desc }

                if (pie != null) {
                    val timePie = pie.data + it.time
                    pies.value.remove(pie)
                    pies.value.add(
                        Pie(
                            label = timeCode.desc,
                            data = timePie,
                            color = Color(timeCode.color)
                        )
                    )
                } else {
                    pies.value.add(
                        Pie(
                            label = timeCode.desc,
                            data = it.time.toDouble(),
                            color = Color(timeCode.color)
                        )
                    )
                }
            }
        }
        return pies
    }

    fun getLegend(): MutableState<MutableMap<String, Long>> {
        val legend = mutableStateOf(mutableMapOf<String, Long>())

        timeCodes.forEach {
            legend.value[it.desc] = it.color
        }
        return legend
    }

    fun getTimeActivity() : MutableState<MutableMap<Long, Float>> {
        val timeActivity = mutableStateOf(mutableMapOf<Long, Float>())

        employeeActivities
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.find { time -> time.idTimeCode == it.idTimeCode }
                if (timeCode != null) {
                    if (timeActivity.value.containsKey(timeCode.color)) {
                        val hours = timeActivity.value[timeCode.color]?.plus(it.time)
                        timeActivity.value[timeCode.color] = hours ?: 0f
                    } else {
                        timeActivity.value[timeCode.color] = it.time
                    }
                }
        }
        return timeActivity
    }

    fun getDayActivity(): MutableState<MutableMap<String, MutableList<Color>>>  {
        val dayActivity = mutableStateOf(mutableMapOf<String, MutableList<Color>>())

        employeeActivities
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.find { time -> time.idTimeCode == it.idTimeCode }
                timeCode?.let { tc ->
                    val currentList = dayActivity.value[it.date] ?: mutableListOf()
                    currentList.add(Color(tc.color))
                    dayActivity.value[it.date] = currentList
                }
            }
        return dayActivity
    }

}