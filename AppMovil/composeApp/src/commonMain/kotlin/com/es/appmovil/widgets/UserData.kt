package com.es.appmovil.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PersonRemove
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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserData(showDialog: Boolean, onChangeDialog: (Boolean) -> Unit) {

    val opciones = listOf("Usuario", "Admin")
    val sheetState = rememberModalBottomSheetState()
    val user by mutableStateOf("")
    var data = false

    var expandido by remember { mutableStateOf(false) }

    val name by mutableStateOf("")
    val lastName1 by mutableStateOf("")
    val lastName2 by mutableStateOf("")
    val email by mutableStateOf("")
    val rol by mutableStateOf("")
    var seleccion by remember { mutableStateOf(opciones[0]) }



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
            text = user,
            modifier = Modifier.weight(1f) // Empuja el botón hacia la derecha
        )
        IconButton(onClick = { data = !data }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Down Arrow"
            )
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
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido },
                modifier = Modifier.padding(16.dp)
            ) {
                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    OutlinedTextField(
                        value = seleccion,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.menuAnchor()
                    )
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

