package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.model.Area
import com.es.appmovil.utils.ManageCSV
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.dailyHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.UserWeekViewModel
import com.es.appmovil.widgets.HeaderSection
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * Pantalla para el control y gestión semanal de usuarios.
 *
 * @param userWeekViewModel ViewModel que maneja la lógica y datos para la vista semanal de usuarios.
 */
class UserWeekScreen(private val userWeekViewModel: UserWeekViewModel) : Screen {

    /**
     * Composable principal que define el contenido de la pantalla.
     * Incluye selección de semana, área, listado de empleados y diálogo para descargar datos CSV.
     */
    @Composable
    override fun Content() {
        val manageCSV = ManageCSV()
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val area by DataViewModel.areas.collectAsState()
        var areaIndex by remember { mutableStateOf(0) }
        var selectedWeek by remember { mutableStateOf(1) }
        var expanded by remember { mutableStateOf(false) }

        var showDialog by remember { mutableStateOf(false) }
        var downloadWeekExpanded by remember { mutableStateOf(false) }

        // Obtiene el primer lunes del año actual para calcular semanas
        val firstDayOfYear = LocalDate(today.value.year, 1, 1)
        val firstMonday = generateSequence(firstDayOfYear) { it.plus(DatePeriod(days = 1)) }
            .first { it.dayOfWeek == DayOfWeek.MONDAY }

        // Calcula primer y último día de la semana seleccionada
        val firstDayOfWeek = firstMonday.plus(DatePeriod(days = (selectedWeek - 1) * 7))
        val lastDayOfWeek = firstDayOfWeek.plus(DatePeriod(days = 6))

        // Muestra diálogo para descargar CSV de la semana seleccionada
        if (showDialog) {
            DownloadWeekDialog(
                selectedWeek = selectedWeek,
                onWeekSelected = { selectedWeek = it },
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    manageCSV.generateWeekCsv(firstDayOfWeek, lastDayOfWeek)
                },
                expanded = downloadWeekExpanded,
                onExpandChange = { downloadWeekExpanded = it },
                firstMonday = firstMonday
            )
        }

        Column(Modifier.fillMaxSize().padding(16.dp)) {

            // Encabezado con título y botón para descargar
            HeaderSection(
                navigator,
                "Control de Semanas",
                Icons.Filled.Download,
                true
            ) { showDialog = true }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de semanas desplegable
            WeekSelector(
                selectedWeek = selectedWeek,
                onWeekSelected = { selectedWeek = it },
                expanded = expanded,
                onExpandChange = { expanded = it },
                firstMonday = firstMonday
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector horizontal de áreas
            AreaSelector(area = area, onAreaSelected = { areaIndex = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Obtiene empleados filtrados por área y rango de fechas
            val employeeByArea = userWeekViewModel.getEmployeeByArea(
                area[areaIndex].desc,
                Pair(firstDayOfWeek, lastDayOfWeek)
            )

            // Lista de empleados con horas trabajadas
            EmployeeList(employeeByArea = employeeByArea)
        }
    }

    /**
     * Composable para seleccionar la semana del año.
     *
     * @param selectedWeek Semana actualmente seleccionada.
     * @param onWeekSelected Callback para actualizar la semana seleccionada.
     * @param expanded Estado para mostrar u ocultar el menú desplegable.
     * @param onExpandChange Callback para cambiar el estado expandido.
     * @param firstMonday Primer lunes del año para calcular fechas.
     */
    @Composable
    private fun WeekSelector(
        selectedWeek: Int,
        onWeekSelected: (Int) -> Unit,
        expanded: Boolean,
        onExpandChange: (Boolean) -> Unit,
        firstMonday: LocalDate
    ) {
        val firstDay = firstMonday.plus(DatePeriod(days = (selectedWeek - 1) * 7))
        val lastDay = firstDay.plus(DatePeriod(days = 6))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Muestra semana seleccionada y abre el menú al hacer clic
            Text("Semana $selectedWeek", modifier = Modifier.clickable { onExpandChange(true) })

            // Muestra rango de fechas de la semana
            Text(
                "${firstDay.dayOfMonth} ${
                    firstDay.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                } - " +
                        "${lastDay.dayOfMonth} ${
                            lastDay.month.name.take(3).lowercase()
                                .replaceFirstChar { it.uppercase() }
                        }"
            )
        }

        // Menú desplegable con las 52 semanas del año
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandChange(false) }) {
            (1..52).forEach { week ->
                val weekStart = firstMonday.plus(DatePeriod(days = (week - 1) * 7))
                val weekEnd = weekStart.plus(DatePeriod(days = 6))
                DropdownMenuItem(onClick = {
                    onWeekSelected(week)
                    onExpandChange(false)
                }) {
                    Text("Semana $week (${weekStart.dayOfMonth} ${
                        weekStart.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                    } - " +
                            "${weekEnd.dayOfMonth} ${
                                weekEnd.month.name.take(3).lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            })"
                    )
                }
            }
        }
    }

    /**
     * Composable para seleccionar un área mediante botones en fila horizontal.
     *
     * @param area Lista de áreas disponibles.
     * @param onAreaSelected Callback para actualizar el índice del área seleccionada.
     */
    @Composable
    private fun AreaSelector(
        area: List<Area>,
        onAreaSelected: (Int) -> Unit
    ) {
        LazyRow(Modifier.fillMaxWidth()) {
            items(area.size) {
                // No muestra área con id 1 (posiblemente área genérica o sin área)
                if (area[it].idArea != 1)
                    Button(onClick = { onAreaSelected(it) }, colors = customButtonColors()) {
                        Text(area[it].desc.take(15)) // Muestra nombre recortado a 15 caracteres
                    }
                Spacer(Modifier.size(8.dp))
            }
        }
    }

    /**
     * Composable que muestra una lista con nombre y horas trabajadas por empleado.
     *
     * @param employeeByArea Mapa donde clave es el nombre del empleado y valor las horas trabajadas.
     */
    @Composable
    private fun EmployeeList(employeeByArea: Map<String, Int>) {
        LazyColumn {
            item {
                ElevatedCard(
                    colors = CardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation(5.dp)
                ) {
                    // Recorre cada empleado mostrando su nombre y horas trabajadas con color según horas
                    employeeByArea.entries.forEach { (name, hours) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name)
                            Text(
                                "$hours hrs",
                                color = if (hours < (dailyHours.value * 5)) Color.Red else Color.Green
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Diálogo para seleccionar una semana y descargar los datos en CSV.
     *
     * @param selectedWeek Semana actualmente seleccionada.
     * @param onWeekSelected Callback para actualizar la semana seleccionada.
     * @param onDismiss Callback para cerrar el diálogo.
     * @param onConfirm Callback para confirmar descarga.
     * @param expanded Estado del menú desplegable dentro del diálogo.
     * @param onExpandChange Callback para cambiar el estado expandido del menú.
     * @param firstMonday Primer lunes del año para cálculo de semanas.
     */
    @Composable
    private fun DownloadWeekDialog(
        selectedWeek: Int,
        onWeekSelected: (Int) -> Unit,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
        expanded: Boolean,
        onExpandChange: (Boolean) -> Unit,
        firstMonday: LocalDate
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Descargar Semana") },
            text = {
                Column {
                    Text("Selecciona una semana:")
                    Spacer(Modifier.height(8.dp))

                    val weekStart = firstMonday.plus(DatePeriod(days = (selectedWeek - 1) * 7))
                    val weekEnd = weekStart.plus(DatePeriod(days = 6))

                    // Muestra la semana seleccionada y permite abrir menú para cambiar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExpandChange(true) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Semana $selectedWeek")
                        Text(
                            "${weekStart.dayOfMonth} ${
                                weekStart.month.name.take(3).lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            } - " +
                                    "${weekEnd.dayOfMonth} ${
                                        weekEnd.month.name.take(3).lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    }"
                        )
                    }

                    // Menú desplegable con opciones de semanas
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { onExpandChange(false) }) {
                        (1..52).forEach { week ->
                            val start = firstMonday.plus(DatePeriod(days = (week - 1) * 7))
                            val end = start.plus(DatePeriod(days = 6))
                            DropdownMenuItem(onClick = {
                                onWeekSelected(week)
                                onExpandChange(false)
                            }) {
                                Text("Semana $week (${start.dayOfMonth} ${
                                    start.month.name.take(3).lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                } - " +
                                        "${end.dayOfMonth} ${
                                            end.month.name.take(3).lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        })"
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = onConfirm, colors = customButtonColors()) {
                    Text("Descargar")
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