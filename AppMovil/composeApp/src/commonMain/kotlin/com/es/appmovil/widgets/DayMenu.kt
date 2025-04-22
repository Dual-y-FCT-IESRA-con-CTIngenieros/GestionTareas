package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.viewmodel.CalendarViewModel
import kotlinx.datetime.LocalDate


/**
 * Funcion que muestra una ventana deslizante para reallenar los campos de un dia
 *
 *@param showDialog Muestra o no el dialogo
 * @param onChangeDialog Función que cambia el valor de showDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDialog(
    showDialog: Boolean,
    day: LocalDate,
    calendarViewModel: CalendarViewModel,
    onChangeDialog: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var comentario by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf(8) }
    var timeCode by rememberSaveable { mutableStateOf(100) }


    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onChangeDialog(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

            DatePickerFieldToModal(Modifier.padding(16.dp), day)


            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberInputField(value = horas, onValueChange = { horas = it })
                DropdownEjemplo(calendarViewModel, onTimeCodeSelected = { timeCode = it })
            }

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(100.dp)
            )

            Button(
                onClick = {
                    println(calendarViewModel.employeeActivity.value)
                    calendarViewModel.addEmployeeActivity(
                        EmployeeActivity(
                            1,
                            1,
                            timeCode,
                            1,
                            horas.toFloat(),
                            day.toString(),
                            comentario
                        )
                    )
                },
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF5B014),
                    contentColor = Color.Black
                )
            ) {
                Text("Guardar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownEjemplo(calendarViewModel: CalendarViewModel, onTimeCodeSelected: (Int) -> Unit) {
    val opciones by calendarViewModel.timeCode.collectAsState()
    val valores = opciones.entries.toList()
    var seleccion by remember { mutableStateOf(valores.firstOrNull()?.value ?: "") }
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccione time code") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            valores.forEach { (codigo , descripcion) ->
                DropdownMenuItem(
                    text = { Text(descripcion) },
                    onClick = {
                        seleccion = descripcion
                        onTimeCodeSelected(codigo)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
fun NumberInputField(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        // TextField numérico
        OutlinedTextField(
            value = value.toString(),
            onValueChange = {
                val num = it.toIntOrNull()
                if (num != null) {
                    onValueChange(num)
                    if (num > 24) onValueChange(24)
                }
                if (num == null) onValueChange(0)
            },
            label = { Text("Horas") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(100.dp).padding(start = 16.dp)
        )
        Column {

            Button(
                onClick = { if (value < 24) onValueChange(value + 1) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(40.dp, 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus"
                )
            }
            Spacer(Modifier.size(5.dp))
            Button(
                onClick = { if (value > 0) onValueChange(value - 1) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(40.dp, 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Minus",
                    modifier = Modifier.size(25.dp)
                )
            }

        }
    }
}
