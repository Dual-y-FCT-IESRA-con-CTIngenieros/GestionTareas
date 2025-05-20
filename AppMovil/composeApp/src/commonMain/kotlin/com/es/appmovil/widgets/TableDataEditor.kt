package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors


@Composable
fun claseTabla(tableName: String): Unit? {
    return when (tableName) {
        "Activity" -> ActivityDataEditor()
        "Aircraft" -> AircraftDataEditor()
        "Area" -> AreaDataEditor()
        "Calendar" -> CalendarDataEditor()
        "Client" -> ClientDataEditor()
        "Employee" -> EmployeeDataEditor()
        "EmployeeActivity" -> EmployeeActivityDataEditor()
        "EmployeeWO" -> EmployeeWODataEditor()
        "EmployeeWorkHours" -> EmployeeWorkHoursDataEditor()
        "Manager" -> ManagerDataEditor()
        "Project" -> ProjectDataEditor()
        "ProjectTimeCode" -> ProjectTimeCodeDataEditor()
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
        DatePickerDialogSample("Date From",dateFrom)
        DatePickerDialogSample("Date To",dateTo)
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
    var idActivity by remember { mutableStateOf("") }

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
        DatePickerDialogSample("Fecha", fecha)
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idActivity,
            onValueChange = { idActivity = it },
            label = { Text("idActivity") },
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
        DatePickerDialogSample("Date From", dateFrom)
        DatePickerDialogSample("Date To", dateTo)
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
fun EmployeeActivityDataEditor() {
    var idEmployee by remember { mutableStateOf("") }
    var idWorkOrder by remember { mutableStateOf("") }
    var idTimeCode by remember { mutableStateOf("") }
    var idActivity by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idEmployee,
            onValueChange = { idEmployee = it },
            label = { Text("idEmployee") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idWorkOrder,
            onValueChange = { idWorkOrder = it },
            label = { Text("idWorkOrder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
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
            value = idActivity,
            onValueChange = { idActivity = it },
            label = { Text("idActivity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = horas,
            onValueChange = { horas = it },
            label = { Text("Horas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DatePickerDialogSample("Fecha", fecha)
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = comentario,
            onValueChange = { comentario = it },
            label = { Text("Comentario") },
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
fun EmployeeWODataEditor() {
    var idWorkOrder by remember { mutableStateOf("") }
    var idEmployee by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val dateFrom = remember { mutableStateOf("") }
    val dateTo = remember { mutableStateOf("") }

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
            value = idEmployee,
            onValueChange = { idEmployee = it },
            label = { Text("idEmployee") },
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
        DatePickerDialogSample("Date From", dateFrom)
        DatePickerDialogSample("Date To", dateTo)
        Button(
            colors = customButtonColors(),
            onClick = { /* Use the variables here */ }
        ){
            Text("Crear")
        }
    }
}

@Composable
fun EmployeeWorkHoursDataEditor() {
    var idEmployee by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf("") }
    val dateFrom = remember { mutableStateOf("") }
    val dateTo = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idEmployee,
            onValueChange = { idEmployee = it },
            label = { Text("IdEmployee") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = horas,
            onValueChange = { horas = it },
            label = { Text("Horas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        DatePickerDialogSample("Date From", dateFrom)
        DatePickerDialogSample("Date To", dateTo)
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
    var idArea by remember { mutableStateOf("") }

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
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idCliente,
            onValueChange = { idCliente = it },
            label = { Text("IdCliente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idArea,
            onValueChange = { idArea = it },
            label = { Text("IdArea") },
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
fun ProjectTimeCodeDataEditor() {
    var idProject by remember { mutableStateOf("") }
    var idTimeCode by remember { mutableStateOf("") }

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
            value = idTimeCode,
            onValueChange = { idTimeCode = it },
            label = { Text("IdTimeCode") },
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
    var idProject by remember { mutableStateOf("") }
    var idAircraft by remember { mutableStateOf("") }

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
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = projectManager,
            onValueChange = { projectManager = it },
            label = { Text("Project Manager") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idProject,
            onValueChange = { idProject = it },
            label = { Text("idProject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = idAircraft,
            onValueChange = { idAircraft = it },
            label = { Text("idAircraft") },
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