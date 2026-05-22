package com.es.appmovil.ui.mytime

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.es.appmovil.ui.theme.BrandLight
import com.es.appmovil.ui.theme.BrandOrange
import com.es.appmovil.data.model.Activity
import com.es.appmovil.data.model.TimeCode
import com.es.appmovil.data.model.TimeRecord

import java.util.Calendar


// ─────────────────────────────────────────
// Pantalla principal
// ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimeScreen(
    viewModel: MyTimeViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Formulario", "Resumen", "Historial")

    // Cuando se selecciona editar desde historial, ir al formulario
    LaunchedEffect(uiState.editingRecord) {
        if (uiState.editingRecord != null) selectedTab = 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Horas",
                        style = MaterialTheme.typography.titleLarge
                        // titleLarge → 20 sp, naranja corporativo
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = BrandOrange,
                    titleContentColor      = BrandLight,
                    actionIconContentColor = BrandLight
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs de navegación
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            if (uiState.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Contenidor con tamaño definido para evitar constraints infinitos
                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedTab) {
                        0 -> FormSection(
                            uiState = uiState,
                            idEmployee = viewModel.idEmployee,
                            onSave = { record ->
                                if (uiState.editingRecord != null) viewModel.updateRecord(record)
                                else viewModel.createRecord(record)
                            },
                            onCancelEdit = { viewModel.cancelEdit() },
                            onTimecodeChanged = { viewModel.loadActivitiesByTimeCode(it) }
                        )
                        1 -> SummarySection(viewModel = viewModel)
                        2 -> HistorySection(
                            records = uiState.records,
                            timeCodes = uiState.timeCodes,
                            activities = uiState.activities,
                            onEdit = { viewModel.startEdit(it) },
                            onDelete = { viewModel.deleteRecord(it) }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// Sección 1: Formulario de registro
// ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSection(
    uiState: MyTimeUiState,
    idEmployee: Int,
    onSave: (TimeRecord) -> Unit,
    onCancelEdit: () -> Unit,
    onTimecodeChanged: (Int?) -> Unit
) {
    val context = LocalContext.current
    val editing = uiState.editingRecord

    var date by remember(editing) { mutableStateOf(editing?.date ?: todayString()) }
    var hours by remember(editing) { mutableStateOf(editing?.time?.toString() ?: "") }
    var selectedWO by remember(editing) { mutableStateOf(editing?.idWorkOrder) }
    var selectedTC by remember(editing) { mutableIntStateOf(editing?.idTimeCode ?: -1) }
    var selectedAct by remember(editing) { mutableIntStateOf(editing?.idActivity ?: -1) }
    var comment by remember(editing) { mutableStateOf(editing?.comment ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    // Cuando cambia el TimeCode, recargar actividades y limpiar actividad seleccionada
    LaunchedEffect(selectedTC) {
        selectedAct = -1
        onTimecodeChanged(if (selectedTC == -1) null else selectedTC)
    }

    // Al cargar en modo edición, precargar actividades del TC del registro
    LaunchedEffect(editing) {
        if (editing?.idTimeCode != null) {
            onTimecodeChanged(editing.idTimeCode)
        }
    }

    // Mostrar éxito temporal
    LaunchedEffect(uiState.formSuccess) {
        if (uiState.formSuccess) {
            date = todayString()
            hours = ""
            selectedWO = null
            selectedTC = -1
            selectedAct = -1
            comment = ""
            formError = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (editing != null) "✏️ Editando registro" else "Registrar Horas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // ── Fecha ──
        OutlinedTextField(
            value = displayDate(date),
            onValueChange = {},
            label = { Text("Fecha") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> date = "%04d-%02d-%02d".format(y, m + 1, d) },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar fecha")
                }
            }
        )

        // ── Horas ──
        OutlinedTextField(
            value = hours,
            onValueChange = { hours = it },
            label = { Text("Horas trabajadas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ── Orden de Trabajo ──
        DropdownField(
            label = "Orden de Trabajo (opcional)",
            options = uiState.workOrders.map { it.idWorkOrder to it.desc },
            selectedKey = selectedWO,
            nullable = true,
            onSelect = { selectedWO = it }
        )

        // ── Código de Tiempo ──
        DropdownField(
            label = "Código de Tiempo (opcional)",
            options = uiState.timeCodes.map { it.idTimeCode.toString() to it.desc },
            selectedKey = if (selectedTC == -1) null else selectedTC.toString(),
            nullable = true,
            onSelect = { selectedTC = it?.toIntOrNull() ?: -1 }
        )

        // ── Actividad (filtrada por TimeCode seleccionado) ──
        if (uiState.loadingActivities) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Text("Cargando actividades…", style = MaterialTheme.typography.bodySmall)
            }
        } else {
            val activityOptions = if (selectedTC == -1) {
                uiState.activities.map { it.idActivity.toString() to it.desc }
            } else {
                uiState.filteredActivities.map { it.idActivity.toString() to it.desc }
            }
            DropdownField(
                label = "Actividad (opcional)",
                options = activityOptions,
                selectedKey = if (selectedAct == -1) null else selectedAct.toString(),
                nullable = true,
                onSelect = { selectedAct = it?.toIntOrNull() ?: -1 }
            )
        }

        // ── Comentario ──
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
            maxLines = 4
        )

        // Error de formulario
        (formError ?: uiState.formError)?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // ── Botones ──
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (editing != null) {
                OutlinedButton(
                    onClick = onCancelEdit,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }
            }
            Button(
                onClick = {
                    val h = hours.toFloatOrNull()
                    if (h == null || h <= 0f) {
                        formError = "Introduce un número de horas válido"
                        return@Button
                    }
                    formError = null
                    onSave(
                        TimeRecord(
                            id = editing?.id,
                            idEmployee = idEmployee,
                            idWorkOrder = selectedWO,
                            idTimeCode = if (selectedTC == -1) null else selectedTC,
                            idActivity = if (selectedAct == -1) null else selectedAct,
                            time = h,
                            date = date,
                            comment = comment.ifBlank { null }
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (editing != null) "Guardar Cambios" else "Registrar Horas")
            }
        }
    }
}

// ─────────────────────────────────────────
// Sección 2: Resumen Anual
// ─────────────────────────────────────────

@Composable
fun SummarySection(viewModel: MyTimeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var trimestral by remember { mutableStateOf(false) }
    var showDesglose by remember { mutableStateOf(false) }
    val summary = remember(uiState.records) {
        viewModel.computeAnnualSummary(currentYear)
    }

    val objetivoAnual     = uiState.objetivoAnual
    val totalParaEmpleado = uiState.totalParaEmpleado
    val horasExtra        = uiState.horasExtra
    val horasEstandar     = uiState.horasEstandar
    val permisosRetrib    = uiState.permisosRetribuidos
    val balance           = uiState.balance
    val horasJornada      = uiState.horasJornada
    val jornadasAnuales   = uiState.jornadasAnuales

    val progreso = if (objetivoAnual > 0f) (totalParaEmpleado / objetivoAnual).coerceIn(0f, 1f) else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Resumen $currentYear", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        // ── Tarjeta de balance enriquecido ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Fila principal: computable vs objetivo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Total computable",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "%.1f h".format(totalParaEmpleado),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Objetivo anual",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "%.0f h  (${horasJornada}h × $jornadasAnuales jornadas)".format(objetivoAnual),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Barra de progreso
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (balance >= 0f) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                // Saldo
                val saldoColor = if (balance >= 0f) MaterialTheme.colorScheme.primary
                                 else MaterialTheme.colorScheme.error
                val saldoTexto = if (balance >= 0f)
                    "+%.1f h sobre el objetivo".format(balance)
                else
                    "%.1f h por debajo del objetivo".format(balance)
                Text(saldoTexto, style = MaterialTheme.typography.bodySmall,
                    color = saldoColor, fontWeight = FontWeight.SemiBold)

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Fila horas estándar + permisos
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Trabajo estándar", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("%.1f h".format(horasEstandar), style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Permisos retribuidos", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("%.1f h".format(permisosRetrib), style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                // Horas extra (informativo, no computa en objetivo)
                if (horasExtra > 0f) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Horas extra (informativo)", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("+%.1f h".format(horasExtra), style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Desglose por código (expandible)
                if (uiState.horasPorCodigo.isNotEmpty()) {
                    TextButton(
                        onClick = { showDesglose = !showDesglose },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            if (showDesglose) "▲ Ocultar desglose" else "▼ Ver desglose por código",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (showDesglose) {
                        uiState.horasPorCodigo.forEach { (nombre, horas) ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• $nombre", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%.1f h".format(horas), style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Toggle Anual / Trimestral
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = !trimestral,
                onClick = { trimestral = false },
                label = { Text("Anual") }
            )
            FilterChip(
                selected = trimestral,
                onClick = { trimestral = true },
                label = { Text("Trimestral") }
            )
        }


        if (!trimestral) {
            // Vista anual: grid 2 columnas
            val chunked = summary.chunked(2)
            chunked.forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { ms ->
                        MonthCard(ms, Modifier.weight(1f))
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        } else {
            // Vista trimestral
            val quarters = listOf(
                "T1 (Ene-Mar)" to summary.subList(0, 3),
                "T2 (Abr-Jun)" to summary.subList(3, 6),
                "T3 (Jul-Sep)" to summary.subList(6, 9),
                "T4 (Oct-Dic)" to summary.subList(9, 12)
            )
            quarters.forEach { (title, months) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            months.forEach { ms -> MonthCard(ms, Modifier.weight(1f)) }
                        }
                        val quarterTotal = months.sumOf { it.hours.toDouble() }.toFloat()
                        Text("Total: %.1f h".format(quarterTotal), style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthCard(ms: MonthSummary, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (ms.hours > 0f) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(ms.name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text("%.1f h".format(ms.hours), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text("${ms.count} reg.", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─────────────────────────────────────────
// Sección 3: Historial
// ─────────────────────────────────────────

@Composable
fun HistorySection(
    records: List<TimeRecord>,
    timeCodes: List<TimeCode>,
    activities: List<Activity>,
    onEdit: (TimeRecord) -> Unit,
    onDelete: (TimeRecord) -> Unit
) {
    if (records.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay registros aún", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    // LazyColumn con fillMaxSize() — tiene altura acotada por el Box padre
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(records, key = { it.id ?: it.date + it.time }) { record ->
            RecordCard(
                record = record,
                timeCodes = timeCodes,
                activities = activities,
                onEdit = { onEdit(record) },
                onDelete = { onDelete(record) }
            )
        }
    }
}

@Composable
fun RecordCard(
    record: TimeRecord,
    timeCodes: List<TimeCode>,
    activities: List<Activity>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar registro?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chip de horas con color del código de tiempo
            val tc = timeCodes.find { it.idTimeCode == record.idTimeCode }
            val chipColor = tc?.let {
                try { Color(it.color) } catch (e: Exception) { MaterialTheme.colorScheme.primaryContainer }
            } ?: MaterialTheme.colorScheme.primaryContainer

            Box(
                modifier = Modifier
                    .background(chipColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    "%.1f h".format(record.time),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(displayDate(record.date), fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                if (!record.idWorkOrder.isNullOrBlank()) {
                    Text("OT: ${record.idWorkOrder}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                tc?.let {
                    Text("CT: ${it.desc}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                val act = activities.find { it.idActivity == record.idActivity }
                act?.let {
                    Text("Act: ${it.desc}", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (!record.comment.isNullOrBlank()) {
                    Text(record.comment, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline)
                }
            }

            // Acciones
            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// Componente: DropdownField genérico
// ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<Pair<String, String>>,   // (key, displayText)
    selectedKey: String?,
    nullable: Boolean = false,
    onSelect: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = options.find { it.first == selectedKey }?.second ?: "Ninguno"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selectedKey != null) displayText else "Ninguno",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (nullable) {
                DropdownMenuItem(
                    text = { Text("Ninguno") },
                    onClick = {
                        onSelect(null)
                        expanded = false
                    }
                )
            }
            options.forEach { (key, text) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onSelect(key)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// Utilidades
// ─────────────────────────────────────────

/** Fecha de hoy en formato interno de la API: YYYY-MM-DD */
private fun todayString(): String {
    val cal = Calendar.getInstance()
    return "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

/**
 * Convierte fecha interna (YYYY-MM-DD) al formato visual dd/MM/yyyy.
 * Si el formato no coincide devuelve la cadena sin cambios.
 */
fun displayDate(apiDate: String): String {
    val parts = apiDate.split("-")
    return if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else apiDate
}

