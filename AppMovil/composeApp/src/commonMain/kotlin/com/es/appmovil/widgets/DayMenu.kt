package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.employee
import kotlinx.datetime.LocalDate


/**
 * Funcion que muestra una ventana deslizante para reallenar los campos de un dia
 *
 *@param showDialog Muestra o no el dialogo
 * @param onChangeDialog Función que cambia el valor de showDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDialog(
    showDialog: Boolean,
    day: LocalDate,
    calendarViewModel: CalendarViewModel,
    onChangeDialog: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var comentario by remember { mutableStateOf("") }
    var horas by remember { mutableStateOf(8) }
    var timeCode by rememberSaveable { mutableStateOf(0) }
    var project by rememberSaveable { mutableStateOf("") }
    calendarViewModel.generarProjectsTimeCode()
    val projectsTimeCodes by calendarViewModel.projectTimeCodeDTO.collectAsState()
    var timeCodeSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var projectSeleccionado by rememberSaveable { mutableStateOf<String?>(null) }


    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onChangeDialog(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

            val dates = remember { mutableStateOf(listOf(day)) }

            DatePickerFieldToModal(Modifier.padding(16.dp), day) {
                dates.value = it
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberInputField(value = horas, onValueChange = { horas = it })
                ProyectoYTimeCodeSelector(projectsTimeCodes, { timeCode = it }, timeCodeSeleccionado, {timeCodeSeleccionado = it}, {projectSeleccionado = it})
            }

            ProjectsSelected(projectsTimeCodes, timeCodeSeleccionado, projectSeleccionado, {project = it}, {projectSeleccionado = it})

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(100.dp)
            )

            Button(
                onClick = {

                    val filter = dates.value.filter { it > calendarViewModel.today.value }

                    if (timeCode != 0 && project.isNotBlank() && filter.isEmpty()) { // CONSULTAR
                        onChangeDialog(false)
                        dates.value.forEach { date ->
                            calendarViewModel.addEmployeeActivity(
                                EmployeeActivity(
                                    employee.idEmployee,
                                    project,
                                    timeCode,
                                    1,
                                    horas.toFloat(),
                                    date.toString(),
                                    comentario
                                )
                            )
                        }

                    }
                },
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF5B014),
                    contentColor = Color.Black
                )
            ) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoYTimeCodeSelector(
    proyectTimecodesDTO: List<ProjectTimeCodeDTO>,
    onTimeCodeSelected: (Int) -> Unit,
    timeCodeSeleccionado: Int?,
    onTimeCodeChange:(Int)->Unit,
    onProyectChange:(String?)->Unit,
) {
    var expandirTimeCode by remember { mutableStateOf(false) }

    // Dropdown de TimeCodes
    ExposedDropdownMenuBox(
        expanded = expandirTimeCode,
        onExpandedChange = { expandirTimeCode = !expandirTimeCode },
        modifier = Modifier.padding(end = 16.dp)
    ) {
        OutlinedTextField(
            value = timeCodeSeleccionado?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccione TimeCode") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTimeCode) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandirTimeCode,
            onDismissRequest = { expandirTimeCode = false }
        ) {
            proyectTimecodesDTO.map { it.idTimeCode }.distinct().forEach { timeCode ->
                DropdownMenuItem(
                    text = { Text(timeCode.toString()) },
                    onClick = {
                        onTimeCodeChange(timeCode)
                        onProyectChange(null)
                        onTimeCodeSelected(timeCode)
                        expandirTimeCode = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsSelected(
    proyectTimecodesDTO: List<ProjectTimeCodeDTO>,
    timeCodeSeleccionado:Int?,
    proyectoSeleccionado:String?,
    onChangeProyect:(String)-> Unit,
    onProjectSelected: (String) -> Unit
) {

    var expandirProyecto by remember { mutableStateOf(false) }

    // Una vez que se selecciona el timeCode, filtrar los proyectos asociados
    val proyectosDisponibles = proyectTimecodesDTO
        .firstOrNull { it.idTimeCode == timeCodeSeleccionado }
        ?.projects
        .orEmpty()


    // Dropdown de Proyectos (solo si ya se eligió un TimeCode)
    ExposedDropdownMenuBox(
        expanded = expandirProyecto,
        onExpandedChange = { expandirProyecto = !expandirProyecto },
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        OutlinedTextField(
            value = proyectoSeleccionado ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccione WorkOrder") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirProyecto) },
            modifier = Modifier.menuAnchor(),
            enabled = timeCodeSeleccionado != null
        )

        ExposedDropdownMenu(
            expanded = expandirProyecto,
            onDismissRequest = { expandirProyecto = false }
        ) {
            proyectosDisponibles.forEach { proyecto ->
                DropdownMenuItem(
                    text = { Text(proyecto) },
                    onClick = {
                        onChangeProyect(proyecto)
                        onProjectSelected(proyecto)
                        expandirProyecto = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownEjemplo(opciones: List<String>, onTimeCodeSelected: (String) -> Unit) {
    var seleccion by remember { mutableStateOf(opciones.firstOrNull() ?: "") }
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccione time code") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { codigo ->
                DropdownMenuItem(
                    text = { Text(codigo) },
                    onClick = {
                        seleccion = codigo
                        onTimeCodeSelected(codigo)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
fun NumberInputField(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        // TextField numérico
        OutlinedTextField(
            value = value.toString(),
            onValueChange = {
                val num = it.toIntOrNull()
                if (num != null) {
                    onValueChange(num)
                    if (num > 12) onValueChange(12)
                }
                if (num == null) onValueChange(0)
            },
            label = { Text("Horas") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(100.dp).padding(start = 16.dp)
        )
        Column {

            Button(
                onClick = { if (value < 12) onValueChange(value + 1) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(40.dp, 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus"
                )
            }
            Spacer(Modifier.size(5.dp))
            Button(
                onClick = { if (value > 0) onValueChange(value - 1) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(40.dp, 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Minus",
                    modifier = Modifier.size(25.dp)
                )
            }

        }
    }
}
