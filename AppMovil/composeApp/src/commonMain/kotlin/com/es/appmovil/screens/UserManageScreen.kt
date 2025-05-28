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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TransferWithinAStation
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.model.Employee
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.EmployeesDataViewModel
import com.es.appmovil.viewmodel.FullScreenLoadingManager
import com.es.appmovil.widgets.DatePickerDialogSample
import com.es.appmovil.widgets.UserData
import com.es.appmovil.widgets.genericFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserManageScreen(private val employeesDataViewModel: EmployeesDataViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        employeesDataViewModel.orderEmployees()

        val navigator: Navigator = LocalNavigator.currentOrThrow
        val actualEmployees by employeesDataViewModel.actualEmployees.collectAsState()
        val exEmployees by employeesDataViewModel.exEmployees.collectAsState()
        val roles by DataViewModel.roles.collectAsState()
        val filter by employeesDataViewModel.filter.collectAsState()

        val opciones = roles.map { it.rol }
        val seleccionInicial = if (opciones.isNotEmpty()) opciones[0] else ""
        var seleccion by remember { mutableStateOf(seleccionInicial) }

        var changeEmployees by remember { mutableStateOf(true) }
        var showDialog by rememberSaveable { mutableStateOf(false) }
        var expandido by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        val employeeText = if (changeEmployees) "Empleados actuales" else "Antiguos empleados"

        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Return")
                }
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
            Row(
                modifier = Modifier.fillMaxWidth(), // Esto es crucial para que funcione
                horizontalArrangement = Arrangement.SpaceBetween, // Distribuye el espacio entre los elementos
                verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos
            ) {
                Text(
                    text = employeeText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
                IconButton(onClick = { changeEmployees = !changeEmployees }) {
                    Icon(
                        Icons.Filled.TransferWithinAStation,
                        contentDescription = "change employees"
                    )
                }
            }

            genericFilter(true, filter) { employeesDataViewModel.changeFilter(it) }

            LazyColumn {
                if (changeEmployees) {
                    val actualEmployeesFilter = if (filter.isNotBlank()) {
                        actualEmployees.filter {
                            val name = (it.nombre + " " + it.apellidos).lowercase()
                            filter.lowercase() in name
                        }
                    } else {
                        actualEmployees
                    }
                    items(actualEmployeesFilter.size) { index ->
                        UserData(index, actualEmployeesFilter[index], roles)
                    }
                } else {
                    val exEmployeesFilter = if (filter.isNotBlank()) {
                        exEmployees.filter {
                            val name = (it.nombre + " " + it.apellidos).lowercase()
                            filter.lowercase() in name
                        }
                    } else {
                        exEmployees
                    }
                    items(exEmployeesFilter.size) { index ->
                        UserData(index, exEmployeesFilter[index], roles)
                    }
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
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors(),
                        value = employeesDataViewModel.idCT.value,
                        onValueChange = { employeesDataViewModel.email.value = it },
                        label = { Text("ID CT") },
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors(),
                        value = employeesDataViewModel.idAirbus.value,
                        onValueChange = { employeesDataViewModel.email.value = it },
                        label = { Text("ID Airbus") },
                    )
                    Row {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = employeesDataViewModel.name.value,
                            onValueChange = { employeesDataViewModel.name.value = it },
                            label = { Text("Nombre") },
                            colors = customTextFieldColors(),
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        OutlinedTextField(
                            modifier = Modifier.weight(2f),
                            colors = customTextFieldColors(),
                            value = employeesDataViewModel.lastName.value,
                            onValueChange = { employeesDataViewModel.lastName.value = it },
                            label = { Text("Apellidos") },
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors(),
                        value = employeesDataViewModel.email.value,
                        onValueChange = { employeesDataViewModel.email.value = it },
                        label = { Text("Correo electrónico") },
                    )
                    DatePickerDialogSample(employeesDataViewModel.dateFrom, "Fecha de antigüedad")
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
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                FullScreenLoadingManager.showLoader()
                                employeesDataViewModel.addEmployee(
                                    Employee(
                                        employeesDataViewModel.employees.value.maxByOrNull { it.idEmployee }!!.idEmployee,
                                        employeesDataViewModel.name.value,
                                        employeesDataViewModel.lastName.value,
                                        employeesDataViewModel.email.value,
                                        employeesDataViewModel.dateFrom.value,
                                        null,
                                        roles.find { it.rol == seleccion }?.idRol ?: -1,
                                        null,
                                        employeesDataViewModel.idCT.value,
                                        employeesDataViewModel.idAirbus.value
                                    )
                                )
                                FullScreenLoadingManager.hideLoader()
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