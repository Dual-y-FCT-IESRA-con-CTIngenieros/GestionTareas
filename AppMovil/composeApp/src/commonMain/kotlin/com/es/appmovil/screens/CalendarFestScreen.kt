package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.database.Database
import com.es.appmovil.model.Calendar
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.CalendarFestViewModel
import com.es.appmovil.viewmodel.DataViewModel.calendar
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.HeaderSection
import com.es.appmovil.widgets.monthNameInSpanish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Pantalla para gestionar el calendario de días festivos.
 * Permite visualizar, añadir, actualizar y eliminar fechas festivas del año seleccionado.
 *
 * @param calendarFestViewModel ViewModel que maneja la lógica y estado de la pantalla.
 */
class CalendarFestScreen(private val calendarFestViewModel: CalendarFestViewModel) : Screen {

    @Composable
    override fun Content() {
        // Flag para evitar cambios simultáneos rápidos en el año
        var monthChangeFlag = true

        // Navegador para navegación en la app
        val navigator: Navigator = LocalNavigator.currentOrThrow

        // Estado de la fecha actual observado
        val fechaActual by today.collectAsState()

        // Estados para mostrar los diálogos: actualizar, eliminar y añadir
        var showDialog by remember { mutableStateOf(false) }
        var showDialogDelete by remember { mutableStateOf(false) }
        var showDialogAdd by remember { mutableStateOf(false) }

        // Índice del festivo seleccionado en la lista
        var festIndex by remember { mutableStateOf(0) }

        // Filtrado y ordenamiento de festivos para el año actual
        val festivos = calendar.value
            .filter { it.idCalendar.toString() == fechaActual.year.toString() }
            .sortedBy { it.date }

        // Diálogo para actualizar la fecha de un festivo
        if (showDialog)
            CalendarDialog(
                text = "Actualizar",
                onDismiss = { showDialog = false },
                onDateSelected = { selectedDate ->
                    val calendarUpdate = Calendar(festivos[festIndex].idCalendar, selectedDate.toString())
                    CoroutineScope(Dispatchers.IO).launch {
                        // Primero elimina el festivo antiguo y luego añade el actualizado
                        Database.deleteCalendar("Calendar", festivos[festIndex])
                        Database.addData("Calendar", calendarUpdate)
                    }
                    // Actualiza la lista localmente
                    calendar.value.remove(festivos[festIndex])
                    calendar.value.add(calendarUpdate)
                    showDialog = false
                }
            )

        // Diálogo para añadir un nuevo festivo
        if (showDialogAdd)
            CalendarDialog(
                text = "Guardar",
                onDismiss = { showDialogAdd = false },
                onDateSelected = { selectedDate ->
                    showDialogAdd = false
                    val calendarUpdate = Calendar(festivos[festIndex].idCalendar, selectedDate.toString())
                    CoroutineScope(Dispatchers.IO).launch {
                        Database.addData("Calendar", calendarUpdate)
                    }
                    calendar.value.add(calendarUpdate)
                }
            )

        // Diálogo para confirmar eliminación de un festivo
        if (showDialogDelete)
            DeleteDialog(
                onConfirm = {
                    showDialogDelete = false
                    CoroutineScope(Dispatchers.IO).launch {
                        Database.deleteCalendar("Calendar", festivos[festIndex])
                    }
                    calendar.value.remove(festivos[festIndex])
                },
                onDismiss = { showDialogDelete = false }
            )

        Column(Modifier.fillMaxSize().padding(16.dp)) {

            // Encabezado con título y botón para añadir festivo
            HeaderSection(
                navigator = navigator,
                "Calendario Festivos",
                icon = Icons.Filled.Add,
                 true
            ) {
                showDialogAdd = true
            }

            // Controles para navegar entre años
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarFestViewModel.onMonthChangePrevious(DatePeriod(years = 1))
                    }
                }) {
                    Text("<", fontSize = 24.sp)
                }
                Text(
                    text = "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { resetToday() }
                )
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarFestViewModel.onMonthChangeFordward(DatePeriod(years = 1))
                    }
                }) {
                    Text(">", fontSize = 24.sp)
                }
            }

            // Lista de días festivos del año
            LazyColumn {
                item {
                    festivos.forEachIndexed { index, fest ->
                        ElevatedCard(
                            colors = CardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.elevatedCardElevation(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(fest.date)

                                Row {
                                    IconButton(onClick = {
                                        showDialog = true
                                        festIndex = index
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarMonth,
                                            contentDescription = "Actualizar festivo"
                                        )
                                    }
                                    IconButton(onClick = {
                                        showDialogDelete = true
                                        festIndex = index
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Eliminar festivo"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Diálogo para seleccionar una fecha en el calendario.
     *
     * @param text Texto del botón de confirmación.
     * @param onDismiss Callback que se ejecuta al cerrar el diálogo sin seleccionar.
     * @param onDateSelected Callback que recibe la fecha seleccionada.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarDialog(
        text: String,
        onDismiss: () -> Unit,
        onDateSelected: (LocalDate) -> Unit
    ) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        onDateSelected(selectedDate)
                    }
                    onDismiss()
                }) {
                    Text(text)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }

    /**
     * Diálogo de confirmación para eliminar un festivo.
     *
     * @param onConfirm Callback que se ejecuta al confirmar la eliminación.
     * @param onDismiss Callback que se ejecuta al cancelar o cerrar el diálogo.
     */
    @Composable
    fun DeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("¿Eliminar este día?") },
            confirmButton = {
                Button(onClick = onConfirm, colors = customButtonColors()) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss, colors = customButtonColors()) {
                    Text("Cancelar")
                }
            }
        )
    }
}
