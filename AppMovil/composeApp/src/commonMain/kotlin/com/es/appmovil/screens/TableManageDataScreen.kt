package com.es.appmovil.screens


import androidx.compose.foundation.background
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.utils.DTOConverter.toEntity
import com.es.appmovil.widgets.DatePickerDialogSample

// Interfaz común
interface TableEntry {
    fun getFieldMap(): Map<String, Any?>
    fun getId(): String // para identificar cada entrada única
}

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

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de registros
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(entries, key = { it.getId() }) { entry ->
                    val en = if (entry is TimeCodeDTO) entry.toEntity() else entry
                    val summary = en.getFieldMap()
                        .entries.joinToString(" | ") { "${it.key}: ${it.value}" }
                    Text(
                        text = summary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedEntry = entry
                                editableFields = entry.getFieldMap()
                            }
                            .padding(8.dp)
                    )
                }
            }

            // Editor dinámico
            selectedEntry?.let { entry ->
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Editando: ${entry.getId()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton({selectedEntry = null}) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                editableFields.forEach { (key, value) ->

                    when (key) {
                        "dateFrom" -> {
                            DatePickerDialogSample(mutableStateOf(value.toString()), "Date From")
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
                                    editableFields = editableFields.toMutableMap().apply {
                                        this[key] = it.value.toLong()
                                    }
                                }
                            )
                        }
                        "chkProd" -> {
                            val currentValue = editableFields[key]?.toString()?.toBooleanStrictOrNull() ?: false
                            Row(Modifier.fillMaxWidth().padding(start = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Check Producción")
                                Checkbox(
                                    checked = currentValue,
                                    onCheckedChange = { checked ->
                                        editableFields = editableFields.toMutableMap().apply {
                                            this[key] = checked
                                        }
                                    }
                                )
                            }
                        }
                        else -> {
                            val currentValue = editableFields[key]?.toString() ?: ""
                            OutlinedTextField(
                                value = currentValue,
                                onValueChange = { newValue ->
                                    editableFields = editableFields.toMutableMap().apply {
                                        this[key] = parseValue(value, newValue)
                                    }
                                },
                                label = { Text(key) },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    onUpdate(entry, editableFields)
                }) {
                    Text("Guardar cambios")
                }
            }
        }
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
