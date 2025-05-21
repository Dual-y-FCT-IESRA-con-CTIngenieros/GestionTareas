package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.model.Employee
import com.es.appmovil.utils.ManageCSV
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.DataViewModel.employees

class GenerateCSVScreen:Screen {
    @Composable
    override fun Content() {
        val manageCSV = ManageCSV()
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            DialogCSV1({showDialog = false}) {
                manageCSV.generateCSV1(it)
                showDialog = false
            }
        }
        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Exportar a CSV",
                    fontWeight = FontWeight.Black,
                    fontSize = 25.sp
                )
            }

            Spacer(Modifier.size(40.dp))

            Button(
                {showDialog = true},
                Modifier.fillMaxWidth(),
                colors = customButtonColors()
            ) {
                Text("Descargar CSV 1")
            }

            Button(
                {manageCSV.generateCSV("Employee")},
                Modifier.fillMaxWidth(),
                colors = customButtonColors()
            ) {
                Text("Descargar CSV 2")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCSV1(
    onDismiss: () -> Unit,
    onAccept: (Int) -> Unit
) {
    val employeesList by employees.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf<Employee?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Descargar CSV")

                Spacer(modifier = Modifier.height(24.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedEmployee?.let { "${it.nombre} ${it.apellidos}" } ?: "Selecciona un empleado",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Empleado") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        employeesList.forEach { employee ->
                            val fullName = "${employee.nombre} ${employee.apellidos}"
                            DropdownMenuItem(onClick = {
                                selectedEmployee = employee
                                expanded = false
                            }) {
                                Text(text = fullName)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss,
                        colors = customButtonColors()) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            selectedEmployee?.let { onAccept(it.idEmployee) }
                        },
                        enabled = selectedEmployee != null,
                        colors = customButtonColors()
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}