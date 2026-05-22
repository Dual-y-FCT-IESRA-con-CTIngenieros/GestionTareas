package com.es.appmovil.ui.mytime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.es.appmovil.data.model.Activity
import com.es.appmovil.data.model.TimeCode
import com.es.appmovil.data.model.TimeRecord
import com.es.appmovil.data.model.WorkOrder
import com.es.appmovil.data.repository.TimeRecordRepository
import com.es.appmovil.utils.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MonthSummary(val name: String, val hours: Float, val count: Int)

data class MyTimeUiState(
    val loading: Boolean = false,
    val records: List<TimeRecord> = emptyList(),
    val workOrders: List<WorkOrder> = emptyList(),
    val timeCodes: List<TimeCode> = emptyList(),
    val activities: List<Activity> = emptyList(),
    val error: String? = null,
    // ── Balance enriquecido (viene del backend) ──
    val horasEstandar: Float = 0f,
    val horasExtra: Float = 0f,
    val permisosRetribuidos: Float = 0f,
    val otrasHoras: Float = 0f,
    val totalParaEmpleado: Float = 0f,
    val totalConExtra: Float = 0f,
    val jornadasAnuales: Int = 224,
    val horasJornada: Int = 8,
    val objetivoAnual: Float = 1792f,
    val arrastreHoras: Int = 0,
    val balance: Float = 0f,
    val horasPorCodigo: Map<String, Float> = emptyMap(),
    // ── Actividades filtradas por el TimeCode seleccionado en el formulario ──
    val filteredActivities: List<Activity> = emptyList(),
    val loadingActivities: Boolean = false,
    // ── Formulario ──
    val editingRecord: TimeRecord? = null,
    val formSuccess: Boolean = false,
    val formError: String? = null
)

class MyTimeViewModel(
    private val tokenManager: TokenManager,
    private val repository: TimeRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyTimeUiState(
        horasJornada = tokenManager.getHorasJornada(),
        objetivoAnual = tokenManager.getHorasTotalesAnuales().toFloat()
    ))
    val uiState: StateFlow<MyTimeUiState> = _uiState.asStateFlow()

    val idEmployee: Int get() = tokenManager.getIdEmployee()
    val nombreCompleto: String
        get() = "${tokenManager.getNombre() ?: ""} ${tokenManager.getApellidos() ?: ""}".trim()

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            val recordsDeferred      = async { repository.getRecordsByEmployee(idEmployee) }
            val workOrdersDeferred   = async { repository.getWorkOrders() }
            val timeCodesDeferred    = async { repository.getTimeCodes() }
            val activitiesDeferred   = async { repository.getActivities() }
            // Balance desde API: objetivoAnual = horasJornada * 224 (no se calcula en local)
            val balanceDeferred      = async { repository.getHoursBalance(idEmployee) }

            val records    = recordsDeferred.await().getOrElse { emptyList() }
            val workOrders = workOrdersDeferred.await().getOrElse { emptyList() }
            val timeCodes  = timeCodesDeferred.await().getOrElse { emptyList() }
            val activities = activitiesDeferred.await().getOrElse { emptyList() }
            val balance    = balanceDeferred.await().getOrNull()

            _uiState.value = _uiState.value.copy(
                loading               = false,
                records               = records.sortedByDescending { it.date },
                workOrders            = workOrders,
                timeCodes             = timeCodes,
                activities            = activities,
                // Campos del balance enriquecido — desde la API, sin cálculos locales
                horasEstandar         = balance?.horasEstandar         ?: _uiState.value.horasEstandar,
                horasExtra            = balance?.horasExtra             ?: _uiState.value.horasExtra,
                permisosRetribuidos   = balance?.permisosRetribuidos    ?: _uiState.value.permisosRetribuidos,
                otrasHoras            = balance?.otrasHoras             ?: _uiState.value.otrasHoras,
                totalParaEmpleado     = balance?.totalParaEmpleado      ?: _uiState.value.totalParaEmpleado,
                totalConExtra         = balance?.totalConExtra          ?: _uiState.value.totalConExtra,
                jornadasAnuales       = balance?.jornadasAnuales        ?: _uiState.value.jornadasAnuales,
                horasJornada          = balance?.horasJornada           ?: tokenManager.getHorasJornada(),
                objetivoAnual         = balance?.objetivoAnual          ?: tokenManager.getHorasTotalesAnuales().toFloat(),
                arrastreHoras         = balance?.arrastreHoras          ?: _uiState.value.arrastreHoras,
                balance               = balance?.balance                ?: _uiState.value.balance,
                horasPorCodigo        = balance?.horasPorCodigo         ?: _uiState.value.horasPorCodigo
            )
        }
    }

    fun createRecord(record: TimeRecord) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(formError = null)
            repository.createRecord(record)
                .onSuccess {
                    loadAllData()
                    _uiState.value = _uiState.value.copy(formSuccess = true, editingRecord = null)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(formError = "Error al guardar el registro")
                }
        }
    }

    fun updateRecord(record: TimeRecord) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(formError = null)
            repository.updateRecord(record)
                .onSuccess {
                    loadAllData()
                    _uiState.value = _uiState.value.copy(formSuccess = true, editingRecord = null)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(formError = "Error al actualizar el registro")
                }
        }
    }

    fun deleteRecord(record: TimeRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
                .onSuccess { loadAllData() }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = "Error al eliminar el registro")
                }
        }
    }

    fun startEdit(record: TimeRecord) {
        _uiState.value = _uiState.value.copy(editingRecord = record)
    }

    fun cancelEdit() {
        _uiState.value = _uiState.value.copy(editingRecord = null)
    }

    fun clearFormSuccess() {
        _uiState.value = _uiState.value.copy(formSuccess = false)
    }

    fun clearFormError() {
        _uiState.value = _uiState.value.copy(formError = null)
    }

    /**
     * Carga las actividades disponibles para el TimeCode seleccionado en el formulario.
     * Si idTimeCode es null o -1 limpia la lista.
     */
    fun loadActivitiesByTimeCode(idTimeCode: Int?) {
        viewModelScope.launch {
            if (idTimeCode == null || idTimeCode == -1) {
                _uiState.value = _uiState.value.copy(filteredActivities = emptyList())
                return@launch
            }
            _uiState.value = _uiState.value.copy(loadingActivities = true)
            val result = repository.getActivitiesByTimeCode(idTimeCode)
            _uiState.value = _uiState.value.copy(
                filteredActivities = result.getOrElse { emptyList() },
                loadingActivities  = false
            )
        }
    }

    /** Calcula el resumen anual agrupado por mes para el año dado */
    fun computeAnnualSummary(year: Int): List<MonthSummary> {
        val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
        return months.mapIndexed { idx, name ->
            val prefix = "$year-${String.format("%02d", idx + 1)}"
            val recs = _uiState.value.records.filter { it.date.startsWith(prefix) }
            MonthSummary(name, recs.sumOf { it.time.toDouble() }.toFloat(), recs.size)
        }
    }
}


