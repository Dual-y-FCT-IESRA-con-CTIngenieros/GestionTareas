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
import androidx.lifecycle.viewModelScope
import com.es.appmovil.database.Database
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Aircraft
import com.es.appmovil.model.Area
import com.es.appmovil.model.Client
import com.es.appmovil.model.Manager
import com.es.appmovil.model.Project
import com.es.appmovil.model.Rol
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.utils.DTOConverter.toDTO
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.FullScreenLoadingManager
import com.es.appmovil.viewmodel.TableManageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun claseTabla(tableName: String): Unit? {
    val viewModel = TableManageViewModel()

    return when (tableName) {
        "Activity" -> ActivityDataEditor(viewModel)
        "Aircraft" -> AircraftDataEditor(viewModel)
        "Area" -> AreaDataEditor(viewModel)
        "Client" -> ClientDataEditor(viewModel)
        "Manager" -> ManagerDataEditor(viewModel)
        "Project" -> ProjectDataEditor(viewModel)
        "Rol" -> RolDataEditor(viewModel)
        "TimeCode" -> TimeCodeDataEditor(viewModel)
        "WorkOrder" -> WorkOrderDataEditor(viewModel)
        else -> null
    }

}

@Composable
fun ActivityDataEditor(viewModel: TableManageViewModel) {
    var idActivity by remember { mutableStateOf("") }
    var idTimeCode by remember { mutableStateOf(mapOf("" to "")) }
    var timeCodeSelection by remember { mutableStateOf(false) }
    val timeCodeData = viewModel.timeCode.value.associate { it.idTimeCode.toString() to it.desc }
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
            opciones = timeCodeData,
            seleccion = idTimeCode.values.first().toString(),
            onExapandedChange = { timeCodeSelection = !timeCodeSelection },
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
        DatePickerDialogSample(dateFrom, "Date From")
        DatePickerDialogSample(dateTo, "Date To")
        Button(
            colors = customButtonColors(),
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newActivity = Activity(
                        idActivity.toInt(),
                        idTimeCode.keys.first().toInt(),
                        descripcion,
                        dateFrom.value,
                        dateTo.value
                    )
                    Database.addData<Activity>("Activity", newActivity)
                    viewModel.activities.value.add(newActivity)
                    FullScreenLoadingManager.hideLoader()
                    idActivity = ""
                    idTimeCode = mapOf("" to "")
                    descripcion = ""
                    dateFrom.value = ""
                    dateTo.value = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun AircraftDataEditor(viewModel: TableManageViewModel) {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newAircraft = Aircraft(
                        idAircraft,
                        descripcion
                    )
                    Database.addData<Aircraft>("Aircraft", newAircraft)
                    viewModel.aircraft.value.add(newAircraft)
                    FullScreenLoadingManager.hideLoader()
                    idAircraft = ""
                    descripcion = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun AreaDataEditor(viewModel: TableManageViewModel) {
    var descripcion by remember { mutableStateOf("") }

    Column {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newArea = Area(
                        (viewModel.area.value.maxByOrNull { it.idArea }?.idArea ?: 0) + 1,
                        descripcion
                    )
                    Database.addData<Area>("Area", newArea)
                    viewModel.area.value.add(newArea)
                    FullScreenLoadingManager.hideLoader()
                    descripcion = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun ClientDataEditor(viewModel: TableManageViewModel) {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newClient = Client(
                        (viewModel.client.value.maxByOrNull { it.idCliente }?.idCliente ?: 0) + 1,
                        nombre = nombreCliente
                    )
                    Database.addData<Client>("Client", newClient)
                    viewModel.client.value.add(newClient)
                    FullScreenLoadingManager.hideLoader()
                    nombreCliente = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun ManagerDataEditor(viewModel: TableManageViewModel) {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newManager = Manager(
                        (viewModel.manager.value.maxByOrNull { it.idManager }?.idManager ?: 0) + 1,
                        nombre = nombre,
                        apellidos = apellidos
                    )
                    Database.addData<Manager>("Manager", newManager)
                    viewModel.manager.value.add(newManager)
                    FullScreenLoadingManager.hideLoader()
                    nombre = ""
                    apellidos = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun ProjectDataEditor(viewModel: TableManageViewModel) {
    var idProject by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf(mapOf("" to "")) }
    var clienteSelection by remember { mutableStateOf(false) }
    val clienteData =
        remember { viewModel.client.value.associate { it.idCliente.toString() to it.nombre } }

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
            opciones = clienteData,
            seleccion = idCliente.values.first().toString(),
            onExapandedChange = { clienteSelection = !clienteSelection },
            onValueChange = { idCliente = it }
        )
        Button(
            colors = customButtonColors(),
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newProject = Project(
                        idProject,
                        descripcion,
                        idCliente.keys.first().toInt()
                    )
                    Database.addData<Project>("Project", newProject)
                    viewModel.project.value.add(newProject)
                    FullScreenLoadingManager.hideLoader()
                    idProject = ""
                    descripcion = ""
                    idCliente = mapOf("" to "")
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun RolDataEditor(viewModel: TableManageViewModel) {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newRol = Rol(
                        (viewModel.rol.value.maxByOrNull { it.idRol }?.idRol ?: 0) + 1,
                        rol = rol
                    )
                    Database.addData<Rol>("Rol", newRol)
                    viewModel.rol.value.add(newRol)
                    FullScreenLoadingManager.hideLoader()
                    rol = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun TimeCodeDataEditor(viewModel: TableManageViewModel) {
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
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newTimeCode = TimeCode(
                        idTimeCode.toInt(),
                        descripcion,
                        color,
                        false
                    )
                    Database.addData<TimeCode>("TimeCode", newTimeCode)
                    viewModel.timeCode.value.add(newTimeCode.toDTO())
                    FullScreenLoadingManager.hideLoader()
                    idTimeCode = ""
                    descripcion = ""
                    color = ""
                }
            }
        ) {
            Text("Crear")
        }
    }
}

@Composable
fun WorkOrderDataEditor(viewModel: TableManageViewModel) {
    var idWorkOrder by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var projectManager by remember { mutableStateOf(mapOf("" to "")) }
    var projectManagerSelection by remember { mutableStateOf(false) }
    val projectManagerData =
        viewModel.manager.value.associate { it.idManager.toString() to "${it.nombre} ${it.apellidos}" }
    var idProject by remember { mutableStateOf(mapOf("" to "")) }
    var projectSelection by remember { mutableStateOf(false) }
    val projectData = viewModel.project.value.associate { it.idProject to it.desc }
    var idAircraft by remember { mutableStateOf(mapOf("" to "")) }
    var aircraftSelection by remember { mutableStateOf(false) }
    val aircraftData = viewModel.aircraft.value.associate { it.idAircraft to it.desc }
    var idArea by remember { mutableStateOf(mapOf("" to "")) }
    var areaSelection by remember { mutableStateOf(false) }
    val areaData = viewModel.area.value.associate { it.idArea.toString() to it.desc }

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
            opciones = projectManagerData,
            seleccion = projectManager.values.first().toString(),
            onExapandedChange = { projectManagerSelection = !projectManagerSelection },
            onValueChange = { projectManager = it }
        )
        DropdownMenu(
            label = "Projects",
            expandido = projectSelection,
            opciones = projectData,
            seleccion = idProject.values.first().toString(),
            onExapandedChange = { projectSelection = !projectSelection },
            onValueChange = { idProject = it }
        )
        DropdownMenu(
            label = "Aircrafts",
            expandido = aircraftSelection,
            opciones = aircraftData,
            seleccion = idAircraft.values.first().toString(),
            onExapandedChange = { aircraftSelection = !aircraftSelection },
            onValueChange = { idAircraft = it }
        )
        DropdownMenu(
            label = "Areas",
            expandido = areaSelection,
            opciones = areaData,
            seleccion = idArea.values.first().toString(),
            onExapandedChange = { areaSelection = !areaSelection },
            onValueChange = { idArea = it }
        )
        Button(
            colors = customButtonColors(),
            onClick = {
                CoroutineScope(viewModel.viewModelScope.coroutineContext).launch {
                    FullScreenLoadingManager.showLoader()
                    val newWorkOrder = WorkOrder(
                        idWorkOrder,
                        descripcion,
                        projectManager.keys.first().toInt(),
                        idProject.keys.first(),
                        idAircraft.keys.first().toInt(),
                        idArea.keys.first().toInt()
                    )
                    Database.addData<WorkOrder>("WorkOrder", newWorkOrder)
                    viewModel.workOrder.value.add(newWorkOrder)
                    FullScreenLoadingManager.hideLoader()
                    idWorkOrder = ""
                    descripcion = ""
                    projectManager = mapOf("" to "")
                    idProject = mapOf("" to "")
                    idAircraft = mapOf("" to "")
                    idArea = mapOf("" to "")
                }
            }
        ) {
            Text("Crear")
        }
    }
}