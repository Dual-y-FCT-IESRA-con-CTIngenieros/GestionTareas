package com.es.appmovil.viewmodel

import com.es.appmovil.viewmodel.DataViewModel.areas
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.workOrders
import kotlinx.datetime.LocalDate

/**
 * ViewModel encargado de obtener las horas trabajadas por empleado en un área y semana concretas.
 */
class UserWeekViewModel {

    /**
     * Obtiene un mapa de empleados con el total de horas trabajadas durante la semana en un área.
     *
     * @param area Nombre del área.
     * @param semana Rango de fechas (inicio y fin de la semana).
     * @return Mapa con el nombre del empleado como clave y total de horas trabajadas como valor.
     */
    fun getEmployeeByArea(area: String, semana: Pair<LocalDate, LocalDate>): Map<String, Int> {
        // Buscamos el id del área a partir de su nombre
        val areaId = areas.value.find { it.desc.equals(area, ignoreCase = true) }?.idArea
            ?: return emptyMap() // Si no existe el área devolvemos un mapa vacío

        // Obtenemos todas las órdenes de trabajo asociadas a esa área
        val workOrdersInArea =
            workOrders.value.filter { it.idArea == areaId }.map { it.idWorkOrder }.toSet()

        val (startDate, endDate) = semana

        // Filtramos las actividades que están dentro del rango de fechas y pertenecen a esas work orders
        val filteredActivities = employeeActivities.value.filter { activity ->
            val activityDate = LocalDate.parse(activity.date)
            activityDate in startDate..endDate && workOrdersInArea.contains(activity.idWorkOrder)
            // Si queremos contar TODAS las horas de la semana, incluso fuera de las órdenes productivas,
            // simplemente eliminar el `&& workOrdersInArea.contains(...)`
        }

        // Filtramos todas las actividades de la semana (sin importar el área)
        val activities = employeeActivities.value.filter {
            val activityDate = LocalDate.parse(it.date)
            activityDate in startDate..endDate
        }

        // Agrupamos todas las actividades de la semana por empleado
        val grouped = activities.groupBy { it.idEmployee }

        // Mapa donde acumularemos el total de horas por empleado
        val employeeHours = mutableMapOf<String, Int>()

        // Recorremos las actividades filtradas por área
        for (activity in filteredActivities) {
            // Obtenemos los datos del empleado
            val employee = employees.value.find { it.idEmployee == activity.idEmployee } ?: continue
            val employeeName = "${employee.nombre} ${employee.apellidos}"

            // Inicializamos el total en 0 si no existía
            var totalTime = employeeHours[employeeName] ?: 0

            // Sumamos todas las horas de ese empleado dentro del rango de fechas
            grouped.forEach { (key, activities) ->
                if (employee.idEmployee == key)
                    totalTime = activities.sumOf { it.time.toInt() }
            }

            // Guardamos el total en el mapa
            employeeHours[employeeName] = totalTime
        }

        // Ordenamos de mayor a menor por horas y lo devolvemos
        return employeeHours.toList().sortedByDescending { it.second }.toMap()
    }
}
