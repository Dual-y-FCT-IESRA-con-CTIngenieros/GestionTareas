package com.es.appmovil.utils

import com.es.appmovil.database.Database.supabase
import com.es.appmovil.saveToDownloads
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.employeesYearData
import com.es.appmovil.viewmodel.DataViewModel.projects
import com.es.appmovil.viewmodel.DataViewModel.timeCodes
import com.es.appmovil.viewmodel.DataViewModel.workOrders
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/**
 * Clase responsable de la generación de archivos CSV a partir de datos de empleados,
 * actividades, proyectos, y órdenes de trabajo. Incluye funciones para exportar reportes
 * semanales, anuales y tablas completas desde Supabase.
 */
class ManageCSV {

    /**
     * Genera un archivo CSV con el resumen anual de horas de recuperación y vacaciones por empleado.
     *
     * @param currentYear Año del que se desea generar el reporte.
     *
     * El archivo generado se guarda con el nombre "Resumen_Anual.csv" en la carpeta de descargas.
     */
    fun generateYearCsv(currentYear: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val data = employees.value.mapNotNull { employee ->
                    // Buscar los datos del empleado y del año actual en employeeActivities
                    val userData = employeesYearData.value
                        .firstOrNull { it.idEmployee == employee.idEmployee && it.year == currentYear }

                    userData?.let {
                        val nombreCompleto = "\"${employee.apellidos}, ${employee.nombre}\""
                        listOf(
                            employee.idCT,
                            nombreCompleto,
                            it.recoveryHours.toString(),
                            it.currentHolidays.toString()
                        )
                    }
                }

                val header = listOf("ID", "Nombre", "Horas Exceso/Recuperar", "Vacaciones / horas")
                val csvContent = buildString {
                    appendLine(header.joinToString(","))
                    data.forEach { row ->
                        appendLine(row.joinToString(","))
                    }
                }

                saveToDownloads(csvContent, "Resumen_Anual.csv")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Genera un archivo CSV con un resumen de actividades realizadas en una semana específica.
     *
     * @param start Fecha de inicio de la semana (inclusive).
     * @param end Fecha de fin de la semana (inclusive).
     *
     * El archivo generado se guarda como "reporte_semanal.csv".
     */
    fun generateWeekCsv(start: LocalDate, end: LocalDate) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val activities = employeeActivities.value.filter {
                    val activityDate = LocalDate.parse(it.date)
                    activityDate in start..end
                }

                // Agrupar por idCT, idTimeCode y idActivity
                val grouped =
                    activities.groupBy { Triple(it.idEmployee, it.idTimeCode, it.idActivity) }

                val csvRows = mutableListOf(
                    listOf(
                        "Usuario",
                        "Nombre",
                        "Tipo",
                        "Proyecto",
                        "Item",
                        "Horas",
                        "DesgloseHoras"
                    )
                )

                grouped.forEach { (key, activities) ->
                    val (employeeId, timeCodeId, activityId) = key
                    val employee =
                        employees.value.find { it.idEmployee == employeeId } ?: return@forEach
                    val timeCode =
                        timeCodes.value.find { it.idTimeCode == timeCodeId } ?: return@forEach

                    val timeTotal = activities.sumOf { it.time.toInt() }

                    val workOrder =
                        workOrders.value.find { it.idWorkOrder == activities.first().idWorkOrder }
                    val project = projects.value.find { it.idProject == workOrder?.idProject }

                    val tipo = when (timeCode.idTimeCode) {
                        100 -> "Trabajo"
                        200 -> "Ausencia"
                        555 -> "Extra"
                        900 -> "Vacaciones"
                        901 -> "Compensación"
                        else -> "Otro"
                    }

                    val desglose = when (activityId) {
                        59 -> "Airbus"
                        514 -> "Baja médica"
                        515 -> "Vacaciones"
                        else -> if (tipo == "Ausencia") "Ausencias" else ""
                    }

                    csvRows.add(
                        listOf(
                            employee.idCT,
                            "\"${employee.apellidos.uppercase()}, ${employee.nombre.uppercase()}\"",
                            tipo,
                            project?.desc ?: "",
                            activities.first().comment ?: "",
                            timeTotal.toString(),
                            desglose
                        )
                    )
                }

                val csvContent = csvRows.joinToString("\n") { row -> row.joinToString(",") }

                saveToDownloads(csvContent, "reporte_semanal.csv")
            } catch (e: Exception) {
                println("Error generando CSV: ${e.message}")
            }
        }
    }

    /**
     * Genera un archivo CSV con todos los datos de una tabla específica de Supabase.
     *
     * @param table Nombre de la tabla a exportar.
     *
     * Recupera los datos en formato JSON, convierte el contenido a CSV y lo almacena como texto.
     */
    fun generateCSV(table: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val jsonData = supabase.from(table).select().data
            val jsonArray = Json.parseToJsonElement(jsonData).jsonArray
            val headers = jsonArray.first().jsonObject.keys

            var csv = ""

            headers.forEachIndexed { index, head ->
                csv += if (index < headers.size - 1) "$head,"
                else "$head\n"
            }

            jsonArray.forEach { elemento ->
                val jsonObject = elemento.jsonObject
                headers.forEachIndexed { index, clave ->
                    csv += if (index < headers.size - 1) "${
                        jsonObject[clave].toString().replace("\"", "")
                    },"
                    else jsonObject[clave].toString().replace("\"", "")
                }
                csv += "\n"
            }
        }
    }

}