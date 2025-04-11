package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate


/**
 * Funcion que muestra una ventana deslizante para reallenar los campos de un dia
 *
 *@param showDialog Muestra o no el dialogo
 * @param onChangeDialog Función que cambia el valor de showDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDialog(showDialog: Boolean, day: LocalDate, onChangeDialog: (Boolean) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var comentario by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf(0) }


    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onChangeDialog(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

            DatePickerFieldToModal(Modifier.padding(16.dp))

            NumberInputField(value = horas, onValueChange = { horas = it })

            DropdownEjemplo()

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(100.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownEjemplo() {
    val opciones = listOf("Opción 1", "Opción 2", "Opción 3")
    var seleccion by remember { mutableStateOf(opciones[0]) }
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
            label = { Text("Selecciona una opción") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        seleccion = opcion
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
                if (num != null) onValueChange(num)
            },
            label = { Text("Horas") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(100.dp).padding(horizontal = 8.dp)
        )
        Column{
            // Botón restar
            Button(onClick = { if (value > 0) onValueChange(value - 1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Minus",
                    modifier = Modifier.size(14.dp)
                )
            }
            // Botón sumar
            Button(onClick = { onValueChange(value + 1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = "Plus",
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
