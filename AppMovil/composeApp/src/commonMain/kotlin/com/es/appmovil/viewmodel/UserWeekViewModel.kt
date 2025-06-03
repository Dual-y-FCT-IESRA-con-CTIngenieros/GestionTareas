package com.es.appmovil.viewmodel

import com.es.appmovil.viewmodel.DataViewModel.areas
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.workOrders
import kotlinx.datetime.LocalDate

class UserWeekViewModel {


    fun getEmployeeByArea(area:String, semana:Pair<LocalDate, LocalDate>):Map<String, Int> {
        val areaId = areas.value.find { it.desc.equals(area, ignoreCase = true) }?.idArea ?: return emptyMap()

        val workOrdersInArea = workOrders.value.filter { it.idArea == areaId }.map { it.idWorkOrder }.toSet()

        val (startDate, endDate) = semana

        val filteredActivities = employeeActivities.value.filter { activity ->
            val activityDate = LocalDate.parse(activity.date)
            activityDate in startDate..endDate && workOrdersInArea.contains(activity.idWorkOrder) // borrar el  && work... para todas las horas aunque no sean productivas
        }

        val employeeHours = mutableMapOf<String, Int>()

        for (activity in filteredActivities) {
            val employee = employees.value.find { it.idEmployee == activity.idEmployee } ?: continue
            val employeeName = "${employee.nombre} ${employee.apellidos}"

            val totalTime = employeeHours[employeeName] ?: 0
            employeeHours[employeeName] = totalTime + activity.time.toInt()
        }

        return employeeHours.toList().sortedByDescending { it.second }.toMap()
    }
}