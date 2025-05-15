package com.es.appmovil.utils

import com.es.appmovil.database.Database.supabase
import com.es.appmovil.model.Activity
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.Project
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.saveToDownloads
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class ManageCSV {

    fun generateCSV(table:String) {
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
                    csv += if (index < headers.size -1) "${jsonObject[clave].toString().replace("\"", "")},"
                    else jsonObject[clave].toString().replace("\"", "")
                }
                csv += "\n"
            }
        }
    }

    fun generateCSV1() {
        CoroutineScope(Dispatchers.Default).launch {
            val json = Json { ignoreUnknownKeys = true }

            // 1. Obtener datos desde Supabase
            val employeeActivities: List<EmployeeActivity> = json.decodeFromString(
                supabase.from("EmployeeActivity").select(){
                    filter { eq("idEmployee", 1) }
                }.data
            )
            val activities: List<Activity> = json.decodeFromString(
                supabase.from("Activity").select().data
            )
            val timeCodes: List<TimeCode> = json.decodeFromString(
                supabase.from("TimeCode").select().data
            )
            val workOrders: List<WorkOrder> = json.decodeFromString(
                supabase.from("WorkOrder").select().data
            )
            val projects: List<Project> = json.decodeFromString(
                supabase.from("Project").select().data
            )

            // 2. Mapas para acceso rápido
            val activityMap = activities.associateBy { it.idActivity }
            val timeCodeMap = timeCodes.associateBy { it.idTimeCode }
            val workOrderMap = workOrders.associateBy { it.idWorkOrder }
            val projectMap = projects.associateBy { it.idProject }

            // 3. Mapear a CSV enriquecido
            val rows = employeeActivities.mapNotNull { ea ->
                val activity = activityMap[ea.idActivity]
                val timeCode = timeCodeMap[ea.idTimeCode]
                val workOrder = workOrderMap[ea.idWorkOrder]
                val project = workOrder?.let { projectMap[it.idProject] }

                val localDate = try {
                    LocalDate.parse(ea.date)
                } catch (e: Exception) {
                    return@mapNotNull null
                }

                val weekKey = localDate.isoWeekKey()
                val dateFormatted =
                    "${localDate.dayOfMonth}/${localDate.monthNumber}/${localDate.year}"

                if (activity != null && timeCode != null && workOrder != null && project != null) {
                    mapOf(
                        "lineType" to "B",
                        "timeCode" to timeCode.idTimeCode.toString(),
                        "timeCodeT" to timeCode.desc,
                        "date" to dateFormatted,
                        "project" to project.idProject,
                        "projectT" to project.desc,
                        "workOrder" to workOrder.idWorkOrder,
                        "workOrderT" to workOrder.desc,
                        "activity" to activity.idActivity.toString(),
                        "activityT" to activity.desc,
                        "hours" to ea.time,
                        "workflowStatusT" to "",
                        "timesheetStatus" to "",
                        "rowStatus" to "",
                        "sequenceNo" to "",
                        "weekKey" to weekKey,
                        "weekHours" to ea.time
                    )
                } else null
            }

            // 4. Agrupar por semana
            val groupedByWeek = rows.groupBy { it["weekKey"] as String }

            // 5. Construir CSV
            val header = listOf(
                "T", "Time code", "Time code (T)", "Trans.date", "Project", "Project (T)",
                "Work order", "Work order (T)", "Activity", "Activity (T)", "Hours",
                "Workflow status (T)", "Timesheet status", "Row status", "Sequence no"
            )

            val csvBuilder = StringBuilder()
            csvBuilder.appendLine(header.joinToString(","))

            groupedByWeek.entries.sortedByDescending { it.key }.forEach { (weekKey, rowsInWeek) ->
                rowsInWeek.forEach { row ->
                    val line = header.map { col ->
                        when (col) {
                            "T" -> row["lineType"]
                            "Time code" -> row["timeCode"]
                            "Time code (T)" -> row["timeCodeT"]
                            "Trans.date" -> row["date"]
                            "Project" -> row["project"]
                            "Project (T)" -> row["projectT"]
                            "Work order" -> row["workOrder"]
                            "Work order (T)" -> row["workOrderT"]
                            "Activity" -> if (row["activity"].toString().length < 3) "0${row["activity"]}" else row["activity"]
                            "Activity (T)" -> row["activityT"]
                            "Hours" -> "\" ${row["hours"].toString().replace(".", ",")} \""
                            "Workflow status (T)" -> row["workflowStatusT"]
                            "Timesheet status" -> row["timesheetStatus"]
                            "Row status" -> row["rowStatus"]
                            "Sequence no" -> row["sequenceNo"]
                            else -> ""
                        }
                    }
                    csvBuilder.appendLine(line.joinToString(","))
                }

                // Total semanal
                val totalHours = rowsInWeek.sumOf { (it["weekHours"] as Float).toDouble() }
                val totalLine = listOf(
                    "", weekKey, "", "", "", "", "", "", "", "",
                    "\" ${totalHours.toString().replace(".", ",")} \"", "", "", "", ""
                )
                csvBuilder.appendLine(totalLine.joinToString(","))
            }

            val csv = csvBuilder.toString()
            saveToDownloads(csv, "DESCARGA2.csv")
        }
    }

    private fun LocalDate.isoWeekKey(): String {
        val dayOfYear = this.dayOfYear
        val dayOfWeek = this.dayOfWeek.isoDayNumber // lunes = 1 ... domingo = 7
        val weekNumber = ((dayOfYear - dayOfWeek + 10) / 7) // regla ISO 8601
        return "${this.year}${weekNumber.toString().padStart(2, '0')}"
    }

    fun generateCSVCustom(tablasSeleccionadas: Map<String, List<String>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val allHeaders = tablasSeleccionadas.values.flatten().toSet().toList() // columnas únicas
            val rows = mutableListOf<Map<String, String>>() // cada fila es un diccionario columna -> valor

            for ((tabla, columnas) in tablasSeleccionadas) {
                val selectedColumns = columnas.joinToString(",")
                val jsonData = supabase.from(tabla).select(Columns.list(selectedColumns)).data
                val jsonArray = Json.parseToJsonElement(jsonData).jsonArray

                for (element in jsonArray) {
                    val jsonObject = element.jsonObject
                    val row = mutableMapOf<String, String>()

                    // Inicializar todas las columnas con vacío
                    allHeaders.forEach { col -> row[col] = "" }

                    // Rellenar las que tiene esta tabla
                    columnas.forEach { col ->
                        val valor = jsonObject[col]?.toString()?.replace("\"", "") ?: ""
                        row[col] = valor
                    }

                    rows.add(row)
                }
            }

            // Construir CSV
            val csvBuilder = StringBuilder()
            csvBuilder.append(allHeaders.joinToString(",")).append("\n")

            rows.forEach { row ->
                val line = allHeaders.joinToString(",") { col -> row[col] ?: "" }
                csvBuilder.append(line).append("\n")
            }

            val finalCSV = csvBuilder.toString()
            println(finalCSV) // o guárdalo
        }
    }

}