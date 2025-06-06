package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.TableManageViewModel


@Composable
fun claseTabla(tableName: String): Unit? {
    return when (tableName) {
        "Activity" -> ActivityDataEditor()
        "Aircraft" -> AircraftDataEditor()
        "Area" -> AreaDataEditor()
        "Calendar" -> CalendarDataEditor()
        "Client" -> ClientDataEditor()
        "Employee" -> EmployeeDataEditor()
        "Manager" -> ManagerDataEditor()
        "Project" -> ProjectDataEditor()
        "Rol" -> RolDataEditor()
        "TimeCode" -> TimeCodeDataEditor()
        "WorkOrder" -> WorkOrderDataEditor()
        else -> null
    }

}

@Composable
fun ActivityDataEditor() {
    var idActivity by remember { mutableStateOf("") }
    var idTimeCode by remember { mutableStateOf("") }
    var timeCodeSelection by remember { mutableStateOf(false) }
    var timeCodeData by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val dateFrom = remember { mutableStateOf("") }
    val dateTo = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idActivity,
            onValueChange = { idActivity = it },
            label = { Text("idActivity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DropdownMenu(
            label = "TimeCodes",
            expandido = timeCodeSelection,
            opciones = emptyList(),
            seleccion = idTimeCode,
            onExapandedChange = { timeCodeSelection = !timeCodeSelection},
            onValueChange = { idTimeCode = it }
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DatePickerDialogSample(dateFrom,"Date From")
        DatePickerDialogSample(dateTo,"Date To")
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun AircraftDataEditor() {
    var idAircraft by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idAircraft,
            onValueChange = { idAircraft = it },
            label = { Text("idAircraft") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun AreaDataEditor() {
    var idArea by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idArea,
            onValueChange = { idArea = it },
            label = { Text("idArea") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun CalendarDataEditor() {
    var idCalendar by remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idCalendar,
            onValueChange = { idCalendar = it },
            label = { Text("idCalendar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DatePickerDialogSample(fecha,"Fecha")
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun ClientDataEditor() {
    var nombreCliente by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = nombreCliente,
            onValueChange = { nombreCliente = it },
            label = { Text("Nombre del cliente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun EmployeeDataEditor() {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val dateFrom = remember { mutableStateOf("") }
    val dateTo = remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var rolSelection by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DatePickerDialogSample( dateFrom,"Fecha antigüedad")
        DatePickerDialogSample(dateTo,"Fecha fin de contrato")
        DropdownMenu(
            label = "Rols",
            expandido = rolSelection,
            opciones = emptyList(),
            seleccion = rol,
            onExapandedChange = { rolSelection = !rolSelection },
            onValueChange = { rol = it }
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun ManagerDataEditor() {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun ProjectDataEditor() {
    var idProject by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf("") }
    var clienteSelection by remember { mutableStateOf(false) }
    var idArea by remember { mutableStateOf("") }
    var areaSelection by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idProject,
            onValueChange = { idProject = it },
            label = { Text("IdProject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DropdownMenu(
            label = "Clients",
            expandido = clienteSelection,
            opciones = emptyList(),
            seleccion = idCliente,
            onExapandedChange = { clienteSelection = !clienteSelection },
            onValueChange = { idCliente = it }
        )
        DropdownMenu(
            label = "Areas",
            expandido = areaSelection,
            opciones = emptyList(),
            seleccion = idArea,
            onExapandedChange = { areaSelection = !areaSelection },
            onValueChange = { idArea = it }
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun RolDataEditor() {
    var rol by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = rol,
            onValueChange = { rol = it },
            label = { Text("Rol") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun TimeCodeDataEditor() {
    var idTimeCode by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idTimeCode,
            onValueChange = { idTimeCode = it },
            label = { Text("idTimeCode") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = color,
            onValueChange = { color = it },
            label = { Text("Color") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun WorkOrderDataEditor() {
    var idWorkOrder by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var projectManager by remember { mutableStateOf("") }
    var projectManagerSelection by remember { mutableStateOf(false) }
    var idProject by remember { mutableStateOf("") }
    var projectSelection by remember { mutableStateOf(false) }
    var idAircraft by remember { mutableStateOf("") }
    var aircraftSelection by remember { mutableStateOf(false) }
    var idArea by remember{ mutableStateOf("") }
    var areaSelection by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idWorkOrder,
            onValueChange = { idWorkOrder = it },
            label = { Text("IdWorkOrder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DropdownMenu(
            label = "Proyect Managers",
            expandido = projectManagerSelection,
            opciones = emptyList(),
            seleccion = projectManager,
            onExapandedChange = { projectManagerSelection = !projectManagerSelection },
            onValueChange = { projectManager = it }
        )
        DropdownMenu(
            label = "Projects",
            expandido = projectSelection,
            opciones = emptyList(),
            seleccion = idProject,
            onExapandedChange = { projectSelection = !projectSelection },
            onValueChange = { idProject = it }
        )
        DropdownMenu(
            label = "Aircrafts",
            expandido = aircraftSelection,
            opciones = emptyList(),
            seleccion = idAircraft,
            onExapandedChange = { aircraftSelection = !aircraftSelection },
            onValueChange = { idAircraft = it }
        )
        DropdownMenu(
            label = "Areas",
            expandido = areaSelection,
            opciones = emptyList(),
            seleccion = idArea,
            onExapandedChange = { areaSelection = !areaSelection },
            onValueChange = { idArea = it }
        )
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}