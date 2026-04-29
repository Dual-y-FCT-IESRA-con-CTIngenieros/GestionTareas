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

    suspend fun updateRecord(record: TimeRecord): Result<TimeRecord> = runCatching {
        api.updateActivity(record)
    }

    suspend fun deleteRecord(record: TimeRecord): Result<Unit> = runCatching {
        api.deleteActivity(record)
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
     * Obtiene el balance de horas del empleado.
     * objetivoAnual es calculado por el backend como horasJornada * 224.
     * NO se usa ninguna lógica local AIRBUS/214 jornadas.
     */
    suspend fun getHoursBalance(idEmployee: Int): Result<HoursBalance> = runCatching {
        api.getHoursBalance(idEmployee)
    }
}

