package com.es.appmovil.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.es.appmovil.model.Employee
import com.es.appmovil.model.Rol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserData(
    showDialog: Boolean,
    index: Int,
    employee: Employee,
    roles: List<Rol>,
    onChangeDialog: (Boolean) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    var expandido by remember { mutableStateOf(false) }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    val opciones = roles.map { it.rol }
    var seleccion by remember { mutableStateOf(roles.find { it.idRol == employee.idRol }?.rol ?: "") }

    val name by mutableStateOf(employee.nombre)
    val lastName by mutableStateOf(employee.apellidos)
    val email by mutableStateOf(employee.email)



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
            text = employee.nombre,
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
            Row{
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = name,
                    onValueChange = {},
                    label = { Text("Nombre") },
                )
                OutlinedTextField(
                    modifier = Modifier.weight(2f),
                    value = lastName,
                    onValueChange = {},
                    label = { Text("Primer apellido") },
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {},
                label = { Text("Correo electrónico") },
            )
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido },
                modifier = Modifier.fillMaxWidth()
            ) {
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
            }
            Row(modifier =  Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End){
                Button(onClick = {}) {
                    Text("Guardar")
                }
                Button(onClick = {}) {
                    Icon(
                        Icons.Filled.PersonRemove,
                        contentDescription = "User Delete",
                        tint = Color.Red
                    )
                }
            }
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
                value = lastName,
                onValueChange = {},
                label = { Text("Primer apellido") },
            )
            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Correo electrónico") },
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

