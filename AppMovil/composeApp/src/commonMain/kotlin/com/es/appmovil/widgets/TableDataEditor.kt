package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idActivity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idTimeCode") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Date From") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Date to") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Button(
            colors = customButtonColors(),
            onClick ={ TODO() }){
            Text("Crear")
        }
    }

}

@Composable
fun AircraftDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun AreaDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun CalendarDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ClientDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Nombre del cliente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }

}

@Composable
fun EmployeeDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun EmployeeActivityDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Comentario") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun EmployeeWODataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdWorkOrder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idEmployee") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Date From") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Date To") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun EmployeeWorkHoursDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdEmployee") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Horas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("DateFrom") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Date to") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ManagerDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Apellidos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

    }
}

@Composable
fun ProjectDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdProject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdCliente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdArea") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ProjectTimeCodeDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdProject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdTimeCode") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun RolDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Rol") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun TimeCodeDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idTimeCode") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Color") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun WorkOrderDataEditor() {
    Column {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("IdWorkOrder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Descripcion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("Project Manager") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idProject") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = "",
            onValueChange = {},
            label = { Text("idAircraft") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}