package com.es.appmovil.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.es.appmovil.model.dto.TimeCodeDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach

class ResumeViewmodel {

    private val employeeActivities = MutableStateFlow(DataViewModel.employeeActivities.value)
    private val employee = DataViewModel.employee
    private val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    private var _dailyHours = MutableStateFlow(8)
    val dailyHours: StateFlow<Int> = _dailyHours

    private var _currentDay = MutableStateFlow(getDays())
    val currentDay: StateFlow<Int> = _currentDay

    private fun getDays():Int {
        var fc = ""
        var days = 0
        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                if (fc != it.date) {
                    days += 1
                    fc = it.date
                }
            }
        return days
    }

    fun getLegend(): MutableState<MutableMap<String, Long>> {
        val legend = mutableStateOf(mutableMapOf<String, Long>())

        timeCodes.value.forEach {
            legend.value[it.idTimeCode.toString()] = it.color
        }
        return legend
    }

    fun getTimeActivity() : MutableState<MutableMap<Long, Float>> {
        val timeActivity = mutableStateOf(mutableMapOf<Long, Float>())

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                if (timeCode != null) {
                    if (timeActivity.value.containsKey(timeCode.color.toLong())) {
                        val hours = timeActivity.value[timeCode.color.toLong()]?.plus(it.time)
                        timeActivity.value[timeCode.color.toLong()] = hours ?: 0f
                    } else {
                        timeActivity.value[timeCode.color.toLong()] = it.time
                    }
                }
            }
        return timeActivity
    }

    fun getDayActivity(): MutableState<MutableMap<String, MutableList<Color>>>  {
        val dayActivity = mutableStateOf(mutableMapOf<String, MutableList<Color>>())

        employeeActivities.value
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                val timeCode = timeCodes.value.find { time -> time.idTimeCode == it.idTimeCode }
                timeCode?.let { tc ->
                    val currentList = dayActivity.value[it.date] ?: mutableListOf()
                    currentList.add(Color(tc.color.toLong()))
                    dayActivity.value[it.date] = currentList
                }
            }
        return dayActivity
    }

}