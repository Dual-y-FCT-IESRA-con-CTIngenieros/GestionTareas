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
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeeActivities
import com.es.appmovil.viewmodel.DataViewModel.timeCodes
import com.es.appmovil.viewmodel.DayMenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


/**
 * Funcion que muestra una ventana deslizante para reallenar los campos de un dia
 *
 *@param showDialog Muestra o no el dialogo
 * @param onChangeDialog Función que cambia el valor de showDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayConfigDialog(
    showDialog: Boolean,
    day: LocalDate,
    calendarViewModel: CalendarViewModel,
    dayMenuViewModel:DayMenuViewModel,
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

@Composable
fun EditableActivityCard(
    activity: EmployeeActivity,
    day:LocalDate,
    calendarViewModel: CalendarViewModel,
    workOrdersTimeCodes: MutableList<ProjectTimeCodeDTO>,
    activitiesTimeCodes: MutableList<ProjectTimeCodeDTO>,
    onChangeDialog: (Boolean) -> Unit,
    onUpdate: (EmployeeActivity) -> Unit
) {
    var horas by remember { mutableStateOf(activity.time) }
    var timeCodeSeleccionado by remember { mutableStateOf(activity.idTimeCode) }
    var workSeleccionado by remember { mutableStateOf(activity.idWorkOrder) }
    var activitySeleccionado by remember { mutableStateOf(activity.idActivity.toString()) }
    var comentario by remember { mutableStateOf(activity.comment ?: "") }
    val dates = remember { mutableStateOf(listOf(day)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("TimeCode: ${activity.idTimeCode}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberInputField(value = horas.toInt(), onValueChange = { horas = it.toFloat() })
            ProyectoYTimeCodeSelector(
                timeCodes.value,
                workOrdersTimeCodes,
                {
                    timeCodeSeleccionado = it
                    onUpdate(activity.copy(idTimeCode = it))
                    workSeleccionado = ""
                    onUpdate(activity.copy(idWorkOrder = ""))
                    activitySeleccionado = ""
                    onUpdate(activity.copy(idActivity = 0))
                },
                timeCodeSeleccionado,
                {},
                { if (it!= null){
                    workSeleccionado = it
                    onUpdate(activity.copy(idWorkOrder = it))}
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
                {}
            ) {
                activitySeleccionado = it
                onUpdate(activity.copy(idActivity = it.toIntOrNull() ?: 59))
            }
        }
        OutlinedTextField(
            value = comentario,
            onValueChange = {
                comentario = it
            },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Save(timeCodeSeleccionado, workSeleccionado , activitySeleccionado, { onChangeDialog(false) }, {
            dates.value.forEach { date ->
                CoroutineScope(Dispatchers.IO).launch {
                    FullScreenLoadingManager.showLoader()
                    Database.deleteEmployeeActivity(activity)
                    FullScreenLoadingManager.hideLoader()
                }
                employeeActivities.value.remove(activity)
                calendarViewModel.addEmployeeActivity(
                    EmployeeActivity(
                        employee.idEmployee,
                        workSeleccionado ,
                        timeCodeSeleccionado,
                        activitySeleccionado.toIntOrNull() ?: 59,
                        horas,
                        date.toString(),
                        comentario
                    )
                )
            }
        })
    }
}






