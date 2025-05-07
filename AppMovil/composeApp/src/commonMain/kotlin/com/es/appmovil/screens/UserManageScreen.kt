package com.es.appmovil.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.database.Database
import com.es.appmovil.model.dto.EmployeeInsertDTO
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.widgets.DatePickerDialogSample
import com.es.appmovil.widgets.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserManageScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        LaunchedEffect(Unit) {
            DataViewModel.cargarEmployees()
            DataViewModel.cargarRoles()
        }

        val employees = DataViewModel.employees.value
        val roles = DataViewModel.roles.value

        val opciones = roles.map { it.rol }
        val seleccionInicial = if (opciones.isNotEmpty()) opciones[1] else ""
        var seleccion by remember { mutableStateOf(seleccionInicial) }

        var name by mutableStateOf("")
        var lastName by mutableStateOf("")
        var email by mutableStateOf("")
        val dateFrom = mutableStateOf("")

        var showDialog by rememberSaveable { mutableStateOf(false) }
        var expandido by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Usuarios",
                    fontWeight = FontWeight.Black,
                    fontSize = 25.sp
                )
                Button(
                    colors = customButtonColors(),
                    onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
            LazyColumn {
                employees.forEachIndexed { index, employee ->
                    item { UserData(index, employee, roles) }
                }
            }
        }
        if (showDialog) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDialog = false
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(8.dp)
                ) {
                    Row{
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            colors = customTextFieldColors(),
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        OutlinedTextField(
                            modifier = Modifier.weight(2f),
                            colors = customTextFieldColors(),
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Primer apellido") },
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors(),
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                    )
                    DatePickerDialogSample(dateFrom)
                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = { expandido = !expandido }
                    ) {
                        OutlinedTextField(
                            value = seleccion,
                            colors = customTextFieldColors(),
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
                    Button(
                        colors = customButtonColors(),
                        border = BorderStroke(0.5.dp, Color.Black),
                        modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            Database.addEmployee(
                                EmployeeInsertDTO(
                                    name,
                                    lastName,
                                    email,
                                    dateFrom.value,
                                    roles.find { it.rol == seleccion }?.idRol ?: -1
                                )
                            )
                        }
                        showDialog = false
                    }
                    ) {
                        Text("Guardar")
                    }
                }

            }
        }
    }
}