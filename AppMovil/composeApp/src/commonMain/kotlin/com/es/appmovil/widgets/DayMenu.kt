package com.es.appmovil.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.activities
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DayMenuViewModel
import kotlinx.datetime.LocalDate


/**
 * Muestra un modal bottom sheet para editar los datos de un día.
 *
 * @param showDialog Controla la visibilidad del diálogo.
 * @param day Día seleccionado.
 * @param dayMenuViewModel ViewModel para manejar el estado y lógica del diálogo.
 * @param calendarViewModel ViewModel para manejar actividades en el calendario.
 * @param onChangeDialog Callback para cambiar el estado de visibilidad.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDialog(
    showDialog: Boolean,
    day: LocalDate,
    dayMenuViewModel: DayMenuViewModel,
    calendarViewModel: CalendarViewModel,
    onChangeDialog: (Boolean) -> Unit
) {
    dayMenuViewModel.generateWorkOrders()
    dayMenuViewModel.generateActivities()
    val sheetState = rememberModalBottomSheetState()
    val comentario by dayMenuViewModel.comment.collectAsState()
    val horas by dayMenuViewModel.hours.collectAsState()

    val timeCode by dayMenuViewModel.timeCode.collectAsState()
    val timeCodes by dayMenuViewModel.timeCodes.collectAsState()
    val timeCodeSeleccionado by dayMenuViewModel.timeCodeSeleccionado.collectAsState()

    val workOrder by dayMenuViewModel.workOrder.collectAsState()
    val workOrdersTimeCodes by dayMenuViewModel.workOrderTimeCodeDTO.collectAsState()
    val workSeleccionado by dayMenuViewModel.workSelected.collectAsState()

    val activity by dayMenuViewModel.activity.collectAsState()
    val activitiesTimeCodes by dayMenuViewModel.activityTimeCode.collectAsState()
    val activitySeleccionado by dayMenuViewModel.activitySelected.collectAsState()

    val startUnblockDate = employee.unblockDate?.split("/")?.get(0) ?: ""
    val endUnblockDate = employee.unblockDate?.split("/")?.get(1) ?: ""


    dayMenuViewModel.loadTimes(100)


    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                onChangeDialog(false)
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {

            val dates = remember { mutableStateOf(listOf(day)) }

            DatePickerFieldToModal(Modifier.padding(horizontal = 16.dp), day) {
                dates.value = it
            }
            Spacer(Modifier.size(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberInputField(value = horas, onValueChange = { dayMenuViewModel.onHours(it) })
                ProyectoYTimeCodeSelector(
                    timecodeData = timeCodes,
                    proyectTimecodesDTO = workOrdersTimeCodes,
                    onTimeCodeChange = { dayMenuViewModel.loadTimes(it) },
                    timeCodeSeleccionado = timeCodeSeleccionado,
                    onTimeCodeSelected = { dayMenuViewModel.onTimeSelected(it) },
                    onProyectChange = {
                    })
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ProjectsSelected(
                    workOrdersTimeCodes,
                    timeCode,
                    workSeleccionado,
                    "WorkOrder",
                    Modifier.weight(1f).padding(start = 16.dp, top = 8.dp),
                    { dayMenuViewModel.onWorkOrder(it) },
                    { dayMenuViewModel.onWorkSelected(it) }
                )
                ProjectsSelected(
                    activitiesTimeCodes,
                    timeCode,
                    activitySeleccionado,
                    "Activity",
                    Modifier.weight(1f).padding(end = 16.dp, top = 8.dp),
                    {
                        val idActivity =
                            activities.value.find { act -> it.split("-")[1].trim() == act.desc }
                        dayMenuViewModel.onActivity(idActivity?.idActivity ?: 0)
                    },
                    { dayMenuViewModel.onActivitySelected(it) })
            }


            OutlinedTextField(
                value = comentario,
                onValueChange = { dayMenuViewModel.onComment(it) },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(100.dp)
            )

            Save(timeCode, workOrder, activity, { onChangeDialog(false) }, {
                dates.value.forEach { date ->
                    val blockDate = employee.blockDate?.let { LocalDate.parse(it) }
                    if (blockDate == null || date > blockDate || employee.unblockDate == null || date.toString() in (startUnblockDate..endUnblockDate)) { // PONER BLOCK DATE NO NULO
                        calendarViewModel.addEmployeeActivity(
                            EmployeeActivity(
                                employee.idEmployee,
                                workOrder,
                                timeCode,
                                activity,
                                horas.toFloat(),
                                date.toString(),
                                comentario
                            )
                        )
                    }

                }
                dayMenuViewModel.clear()
            })
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}
/**
 * Botón que guarda los datos del día si todos los campos requeridos están completos.
 *
 * @param timeCode Código del tiempo seleccionado.
 * @param workOrder Orden de trabajo seleccionada.
 * @param activity Actividad seleccionada.
 * @param onChangeDialog Callback para cerrar el diálogo.
 * @param onSaveEmploye Callback para guardar la actividad.
 */
@Composable
fun Save(
    timeCode: Int,
    workOrder: String,
    activity: Int,
    onChangeDialog: () -> Unit,
    onSaveEmploye: () -> Unit
) {
    Button(
        onClick = {
            if (timeCode != 0 && workOrder.isNotBlank() && activity != 0) {
                onChangeDialog()
                onSaveEmploye()
            }
        },
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFF5B014),
            contentColor = Color.Black
        )
    ) {
        Text("Guardar")
    }
}

/**
 * Selector desplegable para elegir un TimeCode dentro de un proyecto.
 *
 * @param timecodeData Lista de TimeCodes disponibles.
 * @param proyectTimecodesDTO Lista de proyectos con sus TimeCodes.
 * @param onTimeCodeSelected Callback al seleccionar un TimeCode (string).
 * @param timeCodeSeleccionado TimeCode actualmente seleccionado.
 * @param onTimeCodeChange Callback cuando cambia el TimeCode (int).
 * @param onProyectChange Callback cuando cambia el proyecto (string o null).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoYTimeCodeSelector(
    timecodeData: List<TimeCodeDTO>,
    proyectTimecodesDTO: List<ProjectTimeCodeDTO>,
    onTimeCodeSelected: (String?) -> Unit,
    timeCodeSeleccionado: String?,
    onTimeCodeChange: (Int) -> Unit,
    onProyectChange: (String?) -> Unit,
) {
    var expandirTimeCode by remember { mutableStateOf(false) }

    // Dropdown de TimeCodes
    ExposedDropdownMenuBox(
        expanded = expandirTimeCode,
        onExpandedChange = { expandirTimeCode = !expandirTimeCode },
        modifier = Modifier.padding(end = 16.dp)
    ) {
        OutlinedTextField(
            value = if (timeCodeSeleccionado != null) if (timeCodeSeleccionado.isNotBlank()) "$timeCodeSeleccionado - ${timecodeData.find { it.idTimeCode == (timeCodeSeleccionado.toIntOrNull() ?: 0) }?.desc ?: ""}" else timeCodeSeleccionado else "",
            onValueChange = {},
            readOnly = true,
            label = { Text("TimeCode") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTimeCode) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        )

        ExposedDropdownMenu(
            expanded = expandirTimeCode,
            onDismissRequest = { expandirTimeCode = false }
        ) {
            proyectTimecodesDTO.sortedBy { it.idTimeCode }.map { it.idTimeCode }.distinct()
                .forEach { timeCode ->
                    DropdownMenuItem(
                        text = {
                            timecodeData.find { it.idTimeCode == timeCode }?.let { Text(it.desc) }

                        },
                        onClick = {
                            onTimeCodeChange(timeCode)
                            onProyectChange(null)
                            onTimeCodeSelected("$timeCode")
                            expandirTimeCode = false
                        }
                    )
                }
        }
    }
}


/**
 * Selector desplegable para elegir un proyecto asociado a un TimeCode.
 *
 * @param proyectTimecodesDTO Lista de proyectos con TimeCodes.
 * @param timeCodeSeleccionado TimeCode seleccionado.
 * @param proyectoSeleccionado Proyecto seleccionado.
 * @param placeholder Texto de placeholder para el campo.
 * @param modifier Modifier para el componente.
 * @param onChangeProyect Callback al cambiar el proyecto.
 * @param onProjectSelected Callback al seleccionar un proyecto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsSelected(
    proyectTimecodesDTO: List<ProjectTimeCodeDTO>,
    timeCodeSeleccionado: Int,
    proyectoSeleccionado: String?,
    placeholder: String,
    modifier: Modifier,
    onChangeProyect: (String) -> Unit,
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
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (proyectoSeleccionado != null) if (proyectoSeleccionado.length > 10)
                "${proyectoSeleccionado.take(11)}..." else proyectoSeleccionado else "",
            onValueChange = {},
            readOnly = true,
            label = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirProyecto) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            enabled = timeCodeSeleccionado != 0
        )

        ExposedDropdownMenu(
            expanded = expandirProyecto,
            onDismissRequest = { expandirProyecto = false }
        ) {
            proyectosDisponibles.forEach { proyecto ->
                DropdownMenuItem(
                    modifier = Modifier.border(0.5.dp, Color.LightGray),
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


/**
 * Campo numérico para ingresar horas con botones para aumentar o disminuir valor.
 *
 * @param value Valor actual de horas.
 * @param onValueChange Callback para actualizar el valor.
 */

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
