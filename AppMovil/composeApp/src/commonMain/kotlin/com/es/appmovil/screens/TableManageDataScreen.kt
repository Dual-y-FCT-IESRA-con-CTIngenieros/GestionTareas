package com.es.appmovil.screens

import androidx.compose.foundation.background
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.es.appmovil.interfaces.TableEntry
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Project
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.utils.DTOConverter.toEntity
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.TableManageViewModel
import com.es.appmovil.widgets.DatePickerDialogSample
import com.es.appmovil.widgets.DropdownMenu

// Pantalla dinámica
class TableManageDataScreen(
    private val tableName: String,
    private val entries: List<TableEntry>,
    private val onUpdate: (TableEntry, Map<String, Any?>) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        var selectedEntry by remember { mutableStateOf<TableEntry?>(null) }
        var editableFields by remember { mutableStateOf<Map<String, Any?>>(emptyMap()) }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Título
            TitleSection(tableName)

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                TableSection(
                    entries = entries,
                    onEntryClick = { entry ->
                        selectedEntry = entry
                        editableFields = if (entry is TimeCodeDTO) entry.toEntity()
                            .getFieldMap() else entry.getFieldMap()
                    }
                )
            }

            // Editor dinámico
            selectedEntry?.let { entry ->
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Editando: ${entry.getId()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton({ selectedEntry = null }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                editableFields.forEach { (key, value) ->

                    EditableFields(key, editableFields, value, { newValue ->
                        editableFields = editableFields.toMutableMap().apply {
                            this[key] = parseValue(value, newValue)
                        }
                    }, {
                        editableFields = editableFields.toMutableMap().apply {
                            this[key] = it.value.toLong()
                        }
                    }, { checked ->
                        editableFields = editableFields.toMutableMap().apply {
                            this[key] = checked
                        }
                    })
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    onUpdate(entry, editableFields)
                }, colors = customButtonColors()) {
                    Text("Guardar cambios")
                }
            }
        }
    }

    // 🔹 Título de la pantalla
    @Composable
    private fun TitleSection(tableName: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tabla: $tableName",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // 🔹 Encabezados y filas de la tabla
    @Composable
    private fun TableSection(entries: List<TableEntry>, onEntryClick: (TableEntry) -> Unit) {
        val columnHeaders = entries.firstOrNull()?.getFieldMap()?.keys?.toList().orEmpty()
        val scrollState = rememberScrollState()

        Column(modifier = Modifier.fillMaxSize()) {

            // Encabezado fijo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .background(Color.LightGray)
                    .padding(vertical = 8.dp)
            ) {
                columnHeaders.forEach { header ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .width(150.dp)
                            .padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)

            // Grid personalizado con scroll sincronizado
            LazyVerticalGrid(
                columns = GridCells.Fixed(1), // Una sola columna para mantener estructura de fila
                modifier = Modifier.fillMaxSize()
            ) {
                items(entries, key = { it.getId() }) { entry ->
                    val en = if (entry is TimeCodeDTO) entry.toEntity() else entry
                    val fieldMap = en.getFieldMap()

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState)
                                .clickable { onEntryClick(entry) }
                                .padding(vertical = 8.dp)
                        ) {
                            columnHeaders.forEach { key ->
                                Text(
                                    text = fieldMap[key].toString(),
                                    modifier = Modifier
                                        .width(150.dp)
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }

    @Composable
    private fun EditableFields(
        key: String,
        editableFields: Map<String, Any?>,
        value: Any?,
        onChangeValue: (String) -> Unit,
        onColorSelected: (Color) -> Unit,
        onCheckedSelected: (Boolean) -> Unit
    ) {
        when (key) {
            "dateFrom" -> {
                DatePickerDialogSample(mutableStateOf(value.toString()), "Date From")
            }

            "projectManager" -> {
                var projectManager by remember { mutableStateOf(mapOf("" to "")) }
                var projectManagerSelection by remember { mutableStateOf(false) }
                val projectManagerData =
                    TableManageViewModel().manager.value.associate { it.idManager.toString() to "${it.nombre} ${it.apellidos}" }

                DropdownMenu(
                    label = "Proyect Managers",
                    expandido = projectManagerSelection,
                    opciones = projectManagerData,
                    seleccion = projectManager.values.first().toString(),
                    onExapandedChange = {
                        projectManagerSelection = !projectManagerSelection
                    },
                    onValueChange = { projectManager = it }
                )
            }

            "idPoject" -> {
                if (entries.all { it is WorkOrder }) {
                    var idProject by remember { mutableStateOf(mapOf("" to "")) }
                    var projectSelection by remember { mutableStateOf(false) }
                    val projectData =
                        TableManageViewModel().project.value.associate { it.idProject to it.desc }

                    DropdownMenu(
                        label = "Projects",
                        expandido = projectSelection,
                        opciones = projectData,
                        seleccion = idProject.values.first().toString()
                            .ifEmpty { editableFields[key]?.toString() ?: "" },
                        onExapandedChange = { projectSelection = !projectSelection },
                        onValueChange = { idProject = it }
                    )
                } else EditText(key, editableFields, onChangeValue)
            }

            "idAircraft" -> {
                if (entries.all { it is WorkOrder }) {
                    var idAircraft by remember { mutableStateOf(mapOf("" to "")) }
                    var aircraftSelection by remember { mutableStateOf(false) }
                    val aircraftData =
                        TableManageViewModel().aircraft.value.associate { it.idAircraft to it.desc }

                    DropdownMenu(
                        label = "Aircrafts",
                        expandido = aircraftSelection,
                        opciones = aircraftData,
                        seleccion = idAircraft.values.first().toString()
                            .ifEmpty { editableFields[key]?.toString() ?: "" },
                        onExapandedChange = { aircraftSelection = !aircraftSelection },
                        onValueChange = { idAircraft = it }
                    )
                } else EditText(key, editableFields, onChangeValue)
            }

            "idArea" -> {
                if (entries.all { it is WorkOrder }) {
                    var idArea by remember { mutableStateOf(mapOf("" to "")) }
                    var areaSelection by remember { mutableStateOf(false) }
                    val areaData =
                        TableManageViewModel().area.value.associate { it.idArea.toString() to it.desc }

                    DropdownMenu(
                        label = "Areas",
                        expandido = areaSelection,
                        opciones = areaData,
                        seleccion = idArea.values.first().toString()
                            .ifEmpty { editableFields[key]?.toString() ?: "" },
                        onExapandedChange = { areaSelection = !areaSelection },
                        onValueChange = { idArea = it }
                    )
                } else EditText(key, editableFields, onChangeValue)
            }

            "idCliente" -> {
                if (entries.all { it is Project }) {
                    var idCliente by remember { mutableStateOf(mapOf("" to "")) }
                    var clienteSelection by remember { mutableStateOf(false) }
                    val clienteData =
                        remember { TableManageViewModel().client.value.associate { it.idCliente.toString() to it.nombre } }
                    DropdownMenu(
                        label = "Clients",
                        expandido = clienteSelection,
                        opciones = clienteData,
                        seleccion = idCliente.values.first().toString()
                            .ifEmpty { editableFields[key]?.toString() ?: "" },
                        onExapandedChange = { clienteSelection = !clienteSelection },
                        onValueChange = { idCliente = it }
                    )
                } else EditText(key, editableFields, onChangeValue)
            }

            "idTimeCode" -> {
                if (entries.all { it is Activity }) {
                    var idTimeCode by remember { mutableStateOf(mapOf("" to "")) }
                    var timeCodeSelection by remember { mutableStateOf(false) }
                    val timeCodeData =
                        TableManageViewModel().timeCode.value.associate { it.idTimeCode.toString() to it.desc }
                    DropdownMenu(
                        label = "TimeCodes",
                        expandido = timeCodeSelection,
                        opciones = timeCodeData,
                        seleccion = idTimeCode.values.first().toString()
                            .ifEmpty { editableFields[key]?.toString() ?: "" },
                        onExapandedChange = { timeCodeSelection = !timeCodeSelection },
                        onValueChange = { idTimeCode = it }
                    )
                } else EditText(key, editableFields, onChangeValue)
            }

            "dateTo" -> {
                DatePickerDialogSample(mutableStateOf(value.toString()), "Date To")
            }

            "color" -> {
                val color = editableFields[key]
                var selectedColor = if (color is Long) Color(color) else Color.Red

                ColorSelectorRow(
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        onColorSelected.invoke(it)
                    }
                )
            }

            "chkProd" -> {
                val currentValue = editableFields[key]?.toString()?.toBooleanStrictOrNull() ?: false
                Row(
                    Modifier.fillMaxWidth().padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Check Producción")
                    Checkbox(
                        checked = currentValue,
                        onCheckedChange = onCheckedSelected
                    )
                }
            }

            else -> EditText(key, editableFields, onChangeValue)
        }
    }

    @Composable
    private fun EditText(
        key: String,
        editableFields: Map<String, Any?>,
        onChangeValue: (String) -> Unit
    ) {
        val currentValue = editableFields[key]?.toString() ?: ""
        OutlinedTextField(
            value = currentValue,
            onValueChange = onChangeValue,
            label = { Text(key) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
    }

    // Ayudante para convertir String a tipo original
    private fun parseValue(originalValue: Any?, newValue: String): Any? {
        return when (originalValue) {
            is Int -> newValue.toIntOrNull()
            is Float -> newValue.toFloatOrNull()
            is Double -> newValue.toDoubleOrNull()
            is Boolean -> newValue.toBooleanStrictOrNull() ?: (newValue.lowercase() == "true")
            is String -> newValue
            is Long -> newValue.toLongOrNull()
            is ULong -> newValue.toULongOrNull()
            null -> newValue
            else -> newValue // Por defecto como String si tipo no se reconoce
        }
    }

    @Composable
    fun ColorSelectorRow(
        selectedColor: Color,
        onColorSelected: (Color) -> Unit
    ) {
        var showDialog by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        ) {
            Text("Color", modifier = Modifier.weight(1f))

            IconButton(onClick = { showDialog = true }) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(selectedColor, shape = CircleShape)
                )
            }
        }

        if (showDialog) {
            ColorPickerDialog(
                onColorSelected = {
                    onColorSelected(it)
                    showDialog = false
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
    }

    @Composable
    fun ColorPickerDialog(
        onColorSelected: (Color) -> Unit,
        onDismiss: () -> Unit
    ) {
        val colors = listOf(
            Color.Red,             // Hue ~0
            Color(0xFFFF69B4),     // Rosa fuerte (~330)
            Color(0xFF800080),     // Púrpura (~300)
            Color(0xFF4B0082),     // Índigo (~275)
            Color(0xFF4682B4),     // Azul acero (~207)
            Color.Blue,            // Azul (~240)
            Color.Cyan,            // Cian (~180)
            Color(0xFF00FFFF),     // Aqua (~180)
            Color(0xFF00FF7F),     // Verde primavera (~150)
            Color.Green,           // Verde (~120)
            Color(0xFF2E8B57),     // Verde mar oscuro (~146)
            Color(0xFF808000),     // Oliva (~60)
            Color.Yellow,          // Amarillo (~60)
            Color(0xFFFFA500),     // Naranja (~38)
            Color(0xFFD2691E),     // Chocolate (~25)
            Color(0xFF8B4513),     // Marrón (~25)
            Color.Magenta,         // Magenta (~300)
            Color.Gray,            // Gris (sin hue)
            Color.Black,           // Negro (sin hue)
            Color.White            // Blanco (sin hue)
        )

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Selecciona un color") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    content = {
                        items(colors.size) { index ->
                            val color = colors[index]
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { onColorSelected(color) }
                            )
                        }
                    }
                )
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
