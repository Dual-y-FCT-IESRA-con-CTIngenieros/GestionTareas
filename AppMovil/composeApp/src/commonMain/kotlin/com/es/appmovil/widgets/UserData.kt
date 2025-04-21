package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserData(showDialog: Boolean, onChangeDialog: (Boolean) -> Unit) {

    val opciones = listOf("Usuario", "Admin")
    val sheetState = rememberModalBottomSheetState()
    val user by mutableStateOf("")
    var data = false

    val name by mutableStateOf("")
    val lastName1 by mutableStateOf("")
    val lastName2 by mutableStateOf("")
    val email by mutableStateOf("")
    val rol by mutableStateOf("")
    var seleccion by remember { mutableStateOf(opciones[0]) }



    Row(Modifier.fillMaxWidth().padding(16.dp).height(30.dp)) {
        Icon(Icons.Filled.AccountCircle, contentDescription = "User")
        Text(user)
        IconButton(onClick = { data = !data }) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Down Arrow")
        }
    }
    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onChangeDialog(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {},
                label = { Text("Nombre") },
            )
            OutlinedTextField(
                value = lastName1,
                onValueChange = {},
                label = { Text("Primer apellido") },
            )
            OutlinedTextField(
                value = lastName2,
                onValueChange = {},
                label = { Text("Segundo apellido") },
            )
            OutlinedTextField(
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
            Button(onClick = {}) {
                Icon(
                    Icons.Filled.PersonRemove,
                    contentDescription = "User Delete",
                    tint = Color.Red
                )
            }
            Button(onClick = {}) {
                Text("Guardar")
            }

        }
    }
}

