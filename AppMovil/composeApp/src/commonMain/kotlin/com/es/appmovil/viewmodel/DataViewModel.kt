package com.es.appmovil.viewmodel

import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.TimeCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object DataViewModel {

    val employee = Employee(1, "Pepe", "Pepito Pepe", "pepe@pepe.com", "2025-01-01", "2025-12-31", 1)

    val employeeActivities = listOf(
        EmployeeActivity(1, 1, 100, 1, 8f, "2025-04-11", "PERFEE"),
        EmployeeActivity(1, 2, 200, 2, 6f, "2025-04-10", "OK"),
        EmployeeActivity(1, 3, 900, 3, 7f, "2025-04-09", "REV"),
        EmployeeActivity(1, 2, 200, 2, 7f, "2025-04-09", "REV")
    )

    val timeCodes = listOf(
        TimeCode(100, "100", 0xFF00FFFF, false),
        TimeCode(200, "200", 0xFFFF0000, false),
        TimeCode(555, "200", 0xFFFF00FF, false),
        TimeCode(900, "900", 0xFF00FF00, false),
        TimeCode(901, "900", 0xFFF0BB0F, false)
    )

    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    fun getHours() {
        employeeActivities
            .filter { employee.idEmployee == it.idEmployee }
            .forEach {
                _currentHours.value += it.time.toInt()
            }
    }


}