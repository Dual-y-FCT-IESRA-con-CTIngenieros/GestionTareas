package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.es.appmovil.database.Database
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.ProjectTimeCodeDTO
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.activities
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.timeCodes
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.viewmodel.FullScreenLoadingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

/**
 * Muestra un diálogo modal para configurar las actividades de un día específico.
 *
 * @param showDialog Indica si el diálogo debe mostrarse.
 * @param day Día seleccionado para configurar.
 * @param calendarViewModel ViewModel que maneja la lógica del calendario.
 * @param dayMenuViewModel ViewModel que maneja los datos y estados del menú del día.
 * @param onChangeDialog Callback para cambiar el estado de visibilidad del diálogo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayConfigDialog(
    showDialog: Boolean,
    day: LocalDate,
    calendarViewModel: CalendarViewModel,
    dayMenuViewModel: DayMenuViewModel,
    onChangeDialog: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val activities: List<EmployeeActivity> = calendarViewModel.getActivitiesForDate(day)
    val activitiesTimeCodes by dayMenuViewModel.activityTimeCode.collectAsState()
    val workOrdersTimeCodes by dayMenuViewModel.workOrderTimeCodeDTO.collectAsState()


    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = { onChangeDialog(false) },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(activities.size) { index ->
                    val activity = activities.sortedBy { it.idTimeCode }[index]

                    EditableActivityCard(
                        activity = activity,
                        day,
                        calendarViewModel,
                        workOrdersTimeCodes,
                        activitiesTimeCodes,
                        { onChangeDialog(false) }, {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

            }
        }
    }
}

/**
 * Componente que muestra una tarjeta editable para una actividad específica.
 *
 * Permite modificar horas, TimeCode, WorkOrder, Activity y comentario.
 * Actualiza la actividad usando [onUpdate] y puede cerrar el diálogo con [onChangeDialog].
 *
 * @param activity Actividad del empleado a editar.
 * @param day Día asociado a la actividad.
 * @param calendarViewModel ViewModel del calendario.
 * @param workOrdersTimeCodes Lista de proyectos y timecodes para work orders.
 * @param activitiesTimeCodes Lista de proyectos y timecodes para actividades.
 * @param onChangeDialog Callback para cambiar el estado de visibilidad del diálogo.
 * @param onUpdate Callback para actualizar la actividad editada.
 */
@Composable
fun EditableActivityCard(
    activity: EmployeeActivity,
    day: LocalDate,
    calendarViewModel: CalendarViewModel,
    workOrdersTimeCodes: MutableList<ProjectTimeCodeDTO>,
    activitiesTimeCodes: MutableList<ProjectTimeCodeDTO>,
    onChangeDialog: (Boolean) -> Unit,
    onUpdate: (EmployeeActivity) -> Unit
) {
    var horas by remember { mutableStateOf(activity.time) }
    var timeCodeSeleccionado by remember { mutableStateOf(activity.idTimeCode) }
    var timeCodeString by remember { mutableStateOf("$timeCodeSeleccionado") }
    var workSeleccionado by remember { mutableStateOf(activity.idWorkOrder) }
    var activitySeleccionado by remember { mutableStateOf("${activity.idActivity} - ${activities.value.find { activity.idActivity == it.idActivity }?.desc ?: ""}") }
    var activityInt by remember { mutableStateOf(activity.idActivity) }
    var comentario by remember { mutableStateOf(activity.comment ?: "") }
    val dates = remember { mutableStateOf(listOf(day)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            "TimeCode: ${activity.idTimeCode}",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberInputField(value = horas.toInt(), onValueChange = { horas = it.toFloat() })

            ProyectoYTimeCodeSelector(
                timecodeData = timeCodes.value,
                proyectTimecodesDTO = workOrdersTimeCodes,
                onTimeCodeChange = {
                    timeCodeSeleccionado = it
                    workSeleccionado = ""
                    activitySeleccionado = ""
                    onUpdate(activity.copy(idTimeCode = it, idWorkOrder = "", idActivity = 0))
                },
                timeCodeSeleccionado = timeCodeString,
                onTimeCodeSelected = { timeCodeString = it ?: "" },
                onProyectChange = {
                    workSeleccionado = it ?: ""
                    activitySeleccionado = it ?: ""
                    onUpdate(
                        activity.copy(
                            idWorkOrder = workSeleccionado,
                            idActivity = it?.toIntOrNull() ?: 0
                        )
                    )
                }
            )
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ProjectsSelected(
                workOrdersTimeCodes,
                timeCodeSeleccionado,
                workSeleccionado,
                "WorkOrder",
                Modifier.weight(1f).padding(start = 16.dp, top = 8.dp),
                {}
            ) {
                workSeleccionado = it
                onUpdate(activity.copy(idWorkOrder = it))
            }
            ProjectsSelected(
                activitiesTimeCodes,
                timeCodeSeleccionado,
                activitySeleccionado,
                "Activity",
                Modifier.weight(1f).padding(end = 16.dp, top = 8.dp),
                {
                    val idActivity =
                        activities.value.find { act -> it.split("-")[1].trim() == act.desc }
                    activityInt = idActivity?.idActivity ?: 0
                    onUpdate(activity.copy(idActivity = activityInt))
                }
            ) { activitySeleccionado = it }
        }
        OutlinedTextField(
            value = comentario,
            onValueChange = {
                comentario = it
            },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Save(timeCodeSeleccionado, workSeleccionado, activityInt, { onChangeDialog(false) }, {
            dates.value.forEach { date ->
                val blockDate = employee.blockDate?.let { LocalDate.parse(it) }
                if (blockDate == null || date > blockDate) { // PONER BLOCK DATE NO NULO
                    CoroutineScope(Dispatchers.IO).launch {
                        FullScreenLoadingManager.showLoader()
                        Database.deleteEmployeeActivity(activity)
                        FullScreenLoadingManager.hideLoader()
                    }
                    employeeActivities.value.remove(activity)
                    calendarViewModel.addEmployeeActivity(
                        EmployeeActivity(
                            employee.idEmployee,
                            workSeleccionado,
                            timeCodeSeleccionado,
                            activityInt,
                            horas,
                            date.toString(),
                            comentario
                        )
                    )
                }
            }
        })
    }
}






