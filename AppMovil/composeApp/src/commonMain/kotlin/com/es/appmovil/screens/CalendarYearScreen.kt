package com.es.appmovil.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.model.Employee
import com.es.appmovil.utils.ManageCSV
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.CalendarYearViewModel
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.employeesYearData
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.HeaderSection
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Pantalla para la gestión anual del calendario de empleados.
 * Permite navegar entre años disponibles, ver detalles y estadísticas anuales por empleado,
 * filtrar y ordenar la lista de empleados, y cerrar el año con opción de generar uno nuevo.
 *
 * @param calendarYearViewModel ViewModel que maneja la lógica y datos para esta pantalla.
 */
class CalendarYearScreen(private val calendarYearViewModel: CalendarYearViewModel) : Screen {
    @Composable
    override fun Content() {
        // Lista de años disponibles con datos (únicos)
        val availableYears = employeesYearData.value.map { it.year }.distinct()

        // Navegador para control de navegación
        val navigator: Navigator = LocalNavigator.currentOrThrow

        // Control para evitar múltiples cambios de año simultáneos
        var yearChangeFlag = true

        // Fecha actual reactiva
        val fechaActual by today.collectAsState()

        // Lista de empleados reactiva
        val employees by employees.collectAsState()

        // Filtro y estado de búsqueda
        val filter by calendarYearViewModel.filter.collectAsState()
        var selectedEmployee by remember { mutableStateOf<Employee?>(null) }

        // Control para mostrar diálogo de cierre de año
        var showCloseYearDialog by remember { mutableStateOf(false) }

        // Estado que indica si el año actual está cerrado
        val isYearClosed = calendarYearViewModel.isCurrentYearClosed()

        // Control para mostrar filtro y modo de ordenamiento
        var showFilter by remember { mutableStateOf(false) }
        var orderDescendant by remember { mutableStateOf(true) }

        // Control para mostrar diálogo de error o información
        val showDialog by calendarYearViewModel.showDialog.collectAsState()

        // Gestión para generación y descarga CSV
        val manageCSV = ManageCSV()
        var showDialogDownload by rememberSaveable { mutableStateOf(false) }

        // Diálogo para confirmación de descarga CSV del año
        if (showDialogDownload) {
            DownloadWeekDialog(
                onDismiss = { showDialogDownload = false },
                onConfirm = {
                    showDialogDownload = false
                    manageCSV.generateYearCsv(today.value.year)
                },
            )
        }

        // Diálogo para mostrar error al intentar cerrar año sin bloquear semanas
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { calendarYearViewModel.changeDialog(false) },
                title = { Text("Error") },
                text = { Text("Debes bloquear todas las semanas para poder bloquear este año.") },
                confirmButton = {}
            )
        }

        Column(Modifier.fillMaxSize().padding(top = 30.dp, end = 16.dp, start = 16.dp)) {
            // Encabezado con título y botón para descarga CSV
            HeaderSection(
                navigator,
                "Gestión anual",
                Icons.Filled.Download,
                true
            ) { showDialogDownload = true }

            Spacer(Modifier.size(16.dp))

            // Navegación entre años disponibles con botones < y >
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fc = fechaActual
                val fechaAnt = fc.minus(DatePeriod(years = 1)).year
                if (fechaAnt in availableYears) {
                    IconButton(onClick = {
                        if (yearChangeFlag) {
                            yearChangeFlag = false
                            calendarYearViewModel.onYearChangePrevious(DatePeriod(years = 1))
                        }
                    }) {
                        Text("<", fontSize = 24.sp)
                    }
                } else {
                    IconButton(onClick = {}, enabled = false) {
                        Text("<", fontSize = 24.sp, color = Color.White)
                    }
                }

                Text(
                    "${fechaActual.year}",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { resetToday() }
                )

                val fc2 = fechaActual
                val fechaPost = fc2.plus(DatePeriod(years = 1)).year
                if (fechaPost in availableYears) {
                    IconButton(onClick = {
                        if (yearChangeFlag) {
                            yearChangeFlag = false
                            calendarYearViewModel.onYearChangeFordward(DatePeriod(years = 1))
                        }
                    }) {
                        Text(">", fontSize = 24.sp)
                    }
                } else {
                    IconButton(onClick = {}, enabled = false) {
                        Text(">", fontSize = 24.sp, color = Color.White)
                    }
                }
            }

            // Botones para cerrar año o mostrar que está cerrado, más opciones de filtro y orden
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isYearClosed) {
                    Button(
                        onClick = { showCloseYearDialog = true },
                        colors = customButtonColors()
                    ) {
                        Text("Cerrar Año")
                    }
                } else {
                    Button(
                        onClick = {},
                        enabled = false
                    ) {
                        Text("Cerrado")
                    }
                }

                Row {
                    IconButton({
                        showFilter = !showFilter
                        if (filter.isNotBlank()) calendarYearViewModel.changeFilter("")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Filtrar"
                        )
                    }
                    IconButton({ orderDescendant = !orderDescendant }) {
                        Icon(
                            imageVector = if (orderDescendant) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                            contentDescription = "Ordenar"
                        )
                    }
                }
            }

            // Campo para aplicar filtro de búsqueda (nombre empleado)
            if (showFilter) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    OutlinedTextField(
                        value = filter,
                        onValueChange = { calendarYearViewModel.changeFilter(it) },
                        label = { Text("Filtro") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = customTextFieldColors()
                    )
                }
            }

            // Lista de empleados filtrados y ordenados, con selección
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .weight(1f)
            ) {
                val employeesFilter = if (filter.isNotBlank()) employees.filter {
                    val name = (it.nombre + " " + it.apellidos).lowercase()
                    filter.lowercase() in name
                } else employees

                val employeesOrder =
                    if (!orderDescendant) employeesFilter.sortedByDescending { it.nombre.uppercase() }
                    else employeesFilter.sortedBy { it.nombre.uppercase() }

                items(employeesOrder.size) { index ->
                    val employee = employeesOrder[index]
                    ElevatedCard(
                        colors = CardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(50.dp)
                            .clickable {
                                selectedEmployee =
                                    if (selectedEmployee != employee) employee else null
                            },
                        elevation = CardDefaults.elevatedCardElevation(5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${employee.nombre} ${employee.apellidos}")
                        }
                    }
                }
            }

            // Resumen de datos anuales del empleado seleccionado
            Box(
                modifier = Modifier.fillMaxSize().padding(bottom = 16.dp).weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                selectedEmployee?.let { employee ->
                    val yearData = calendarYearViewModel.getEmployeeYearData(employee.idEmployee)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Resumen de ${employee.nombre} ${employee.apellidos}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val cards = listOf(
                            "Días Totales" to (yearData?.daysHolidays?.toString() ?: "-"),
                            "V. Disfrutadas" to (yearData?.enjoyedHolidays?.toString() ?: "-"),
                            "V. Restantes" to (yearData?.currentHolidays?.toString() ?: "-"),
                            "H. Trabajadas" to (yearData?.workedHours?.toString() ?: "-"),
                            "H. Exceso/Recuperar" to (yearData?.recoveryHours?.toString() ?: "-")
                        )

                        cards.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { (label, value) ->
                                    Card(
                                        modifier = Modifier.weight(1f),
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = label, fontSize = 14.sp, color = Color.Gray)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = value,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black
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

        // Diálogo para confirmar cierre de año y generar nuevo año si se desea
        if (showCloseYearDialog) {
            var vacationDaysInput by remember { mutableStateOf("") }
            var generateNewYear by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showCloseYearDialog = false },
                title = { Text("Cerrar año") },
                text = {
                    Column {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("¿Generar nuevo año con el mismo calendario?")
                            Checkbox(
                                checked = generateNewYear,
                                onCheckedChange = {
                                    generateNewYear = !generateNewYear
                                    vacationDaysInput = ""
                                }
                            )
                        }
                        Spacer(Modifier.size(5.dp))
                        if (generateNewYear) {
                            Text("Introduce los días de vacaciones para el nuevo año:")
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = vacationDaysInput,
                                onValueChange = { vacationDaysInput = it },
                                label = { Text("Días de vacaciones") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val dias = vacationDaysInput.toIntOrNull() ?: 0
                            calendarYearViewModel.setNextHolidaysDays(dias)
                            calendarYearViewModel.closeYear(generateNewYear, today.value.year)
                            showCloseYearDialog = false
                        },
                        enabled = !generateNewYear || (vacationDaysInput.toIntOrNull() ?: 0) > 0
                    ) {
                        Text("Cerrar año")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showCloseYearDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    /**
     * Diálogo para confirmar la descarga de los datos del año en formato CSV.
     */
    @Composable
    private fun DownloadWeekDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Descargar Datos de este año.") },
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