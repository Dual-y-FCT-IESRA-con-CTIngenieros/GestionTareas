package com.es.appmovil.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.es.appmovil.database.Database
import com.es.appmovil.model.Employee
import com.es.appmovil.model.Rol
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserData(
    index: Int,
    employee: Employee,
    roles: List<Rol>
) {

    var expandido by remember { mutableStateOf(false) }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    var alertOpen by remember { mutableStateOf(false) }

    val opciones = roles.map { it.rol }
    var seleccion by remember {
        mutableStateOf(
            roles.find { it.idRol == employee.idRol }?.rol ?: ""
        )
    }

    val name by mutableStateOf(employee.nombre)
    val lastName by mutableStateOf(employee.apellidos)
    val email by mutableStateOf(employee.email)
    val dateFrom = remember { mutableStateOf(employee.dateFrom) }
    val dateTo = remember { mutableStateOf("") }


    if(employee.dateTo != null){
      if (isDateBeforeToday(employee.dateTo)){
          return
      }
    }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${employee.nombre} ${employee.apellidos}",
                modifier = Modifier.weight(1f) // Empuja el botón hacia la derecha
            )
            IconButton(onClick = {
                expandedIndex = if (expandedIndex == index) null else index
            }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down Arrow"
                )
            }
        }
        if (expandedIndex == index) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Row {
                    OutlinedTextField(
                        colors = customTextFieldColors(),
                        modifier = Modifier.weight(1f),
                        value = name,
                        onValueChange = {},
                        label = { Text("Nombre") },
                    )
                    OutlinedTextField(
                        colors = customTextFieldColors(),
                        modifier = Modifier.weight(2f),
                        value = lastName,
                        onValueChange = {},
                        label = { Text("Primer apellido") },
                    )
                }
                OutlinedTextField(
                    colors = customTextFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {},
                    label = { Text("Correo electrónico") },
                )
                DatePickerDialogSample("Date From",dateFrom)
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        colors = customTextFieldColors(),
                        value = seleccion,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { androidx.compose.material.Text(opcion) },
                                onClick = {
                                    seleccion = opcion
                                    expandido = false
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        colors = customButtonColors(),
                        onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            Database.updateEmployee(
                                EmployeeUpdateDTO(
                                    employee.idEmployee,
                                    name,
                                    lastName,
                                    email,
                                    dateFrom.value,
                                    dateTo.value,
                                    roles.find { it.rol == seleccion }?.idRol ?: -1
                                )
                            )
                        }
                    }) {
                        Text("Guardar")
                    }
                    Button(
                        colors = customButtonColors(),
                        onClick = {
                        alertOpen = true
                    }) {
                        Icon(
                            Icons.Filled.PersonRemove,
                            contentDescription = "User Delete",
                            tint = Color.Red
                        )
                    }
                    confirmRemove(alertOpen, dateTo, EmployeeUpdateDTO(
                        employee.idEmployee,
                        name,
                        lastName,
                        email,
                        dateFrom.value,
                        dateTo.value,
                        roles.find { it.rol == seleccion }?.idRol ?: -1
                    )
                    ){ alertOpen = it }
                }
            }
        }
}

@Composable
fun confirmRemove(alertOpen: Boolean, dateTo: MutableState<String>, employee: EmployeeUpdateDTO, onDismissRequest: (Boolean) -> Unit = {}) {
    if(alertOpen){
        Dialog(onDismissRequest = { onDismissRequest(false) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "¿Seleccione la fecha de fin de contrato?",
                        style = MaterialTheme.typography.titleMedium
                    )

                    DatePickerDialogSample(
                        "Date to",
                        dateTo
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { onDismissRequest(false) }) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            colors = customButtonColors(),
                            onClick = {
                            employee.dateTo = dateTo.value
                            CoroutineScope(Dispatchers.Main).launch {
                                Database.updateEmployee(
                                    employee
                                )
                            }
                            onDismissRequest(false)
                        }) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogSample(label: String, dateFrom: MutableState<String>) {
    var showDatePicker by remember { mutableStateOf(false) }

    val initialMillis = remember(dateFrom.value) {
        try {
            val localDate = LocalDate.parse(dateFrom.value)
            localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        } catch (e: Exception) {
            null
        }
    }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    OutlinedTextField(
        value = dateFrom.value,
        colors = customTextFieldColors(),
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val localDate = Instant.fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        dateFrom.value = localDate.toString() // solo yyyy-MM-dd
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

fun isDateBeforeToday(dateStr: String): Boolean {
    return try {
        val inputDate = LocalDate.parse(dateStr)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        inputDate < today
    } catch (e: Exception) {
        false
    }
}



