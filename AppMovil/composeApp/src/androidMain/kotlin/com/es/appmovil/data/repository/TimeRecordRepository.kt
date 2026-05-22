package com.es.appmovil.data.repository

import com.es.appmovil.data.model.Activity
import com.es.appmovil.data.model.HoursBalance
import com.es.appmovil.data.model.TimeCode
import com.es.appmovil.data.model.TimeRecord
import com.es.appmovil.data.model.WorkOrder
import com.es.appmovil.data.remote.ApiService

class TimeRecordRepository(private val api: ApiService) {

    suspend fun getRecordsByEmployee(idEmployee: Int): Result<List<TimeRecord>> = runCatching {
        api.getActivitiesByEmployee(idEmployee)
    }

    suspend fun createRecord(record: TimeRecord): Result<TimeRecord> = runCatching {
        api.createActivity(record)
    }

    /**
     * Actualiza un registro por ID (PUT /employee-activities/{id}).
     * Requiere que record.id no sea nulo.
     */
    suspend fun updateRecord(record: TimeRecord): Result<TimeRecord> = runCatching {
        val id = record.id ?: error("No se puede actualizar un registro sin ID")
        api.updateActivityById(id, record)
    }

    /**
     * Elimina un registro por ID (DELETE /employee-activities/{id}).
     * Requiere que record.id no sea nulo.
     */
    suspend fun deleteRecord(record: TimeRecord): Result<Unit> = runCatching {
        val id = record.id ?: error("No se puede eliminar un registro sin ID")
        api.deleteActivityById(id)
    }

    suspend fun getWorkOrders(): Result<List<WorkOrder>> = runCatching {
        api.getWorkOrders()
    }

    suspend fun getTimeCodes(): Result<List<TimeCode>> = runCatching {
        api.getTimeCodes()
    }

    suspend fun getActivities(): Result<List<Activity>> = runCatching {
        api.getActivities()
    }

    /**
     * Actividades disponibles para un código de tiempo concreto.
     * GET /api/activities/by-timecode/{idTimeCode}
     */
    suspend fun getActivitiesByTimeCode(idTimeCode: Int): Result<List<Activity>> = runCatching {
        api.getActivitiesByTimeCode(idTimeCode)
    }

    /**
     * Obtiene el balance de horas enriquecido del empleado.
     * objetivoAnual = horasJornada * jornadasAnuales (Config, no hardcodeado).
     * @param year año a consultar; null = año actual (default del backend)
     */
    suspend fun getHoursBalance(idEmployee: Int, year: Int? = null): Result<HoursBalance> = runCatching {
        api.getHoursBalance(idEmployee, year)
    }
}

