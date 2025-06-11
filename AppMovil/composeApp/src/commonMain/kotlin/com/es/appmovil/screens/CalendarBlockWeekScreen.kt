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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.CalendarBlockWeekViewModel
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.HeaderSection
import com.es.appmovil.widgets.monthNameInSpanish
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate

/**
 * Pantalla para gestionar el bloqueo semanal del calendario de empleados.
 * Permite visualizar semanas del mes, bloquear o desbloquear semanas,
 * así como modificar el estado de bloqueo de empleados individualmente.
 *
 * @param calendarBlockWeekViewModel ViewModel que maneja la lógica y estado de la pantalla.
 */
class CalendarBlockWeekScreen(private val calendarBlockWeekViewModel: CalendarBlockWeekViewModel) : Screen {

    @Composable
    override fun Content() {
        // Flag para evitar cambios múltiples simultáneos en el mes
        var monthChangeFlag = true

        // Navegador para navegación en la aplicación
        val navigator: Navigator = LocalNavigator.currentOrThrow

        // Estado observado de la fecha actual
        val fechaActual by today.collectAsState()

        // Estado para mostrar el diálogo de bloqueo
        val showDialog by calendarBlockWeekViewModel.showDialog.collectAsState()

        // Índice de la semana seleccionada en el listado de semanas
        val weekIndex by calendarBlockWeekViewModel.weekIndex.collectAsState()

        // Lista de semanas bloqueadas
        val locked by calendarBlockWeekViewModel.locked.collectAsState()

        // Estado para saber si se está modificando un empleado
        val employeeModifie by calendarBlockWeekViewModel.employeeModifie.collectAsState()

        // Listado de pares de fechas que representan las semanas del mes actual
        val weeksInMonth by calendarBlockWeekViewModel.weeksInMonth.collectAsState()

        // Lista completa de empleados
        val employees by employees.collectAsState()

        // Filtro para búsqueda de empleados
        val filter by calendarBlockWeekViewModel.filter.collectAsState()

        // Inicializa o actualiza el estado de bloqueo
        calendarBlockWeekViewModel.generateLock()

        // Mostrar diálogo de confirmación de bloqueo/desbloqueo si está activo
        if (showDialog) {
            // Obtener fecha para bloqueo/desbloqueo
            val generateBlock by calendarBlockWeekViewModel.blockDate.collectAsState()
            val date = generateBlock?.let {
                runCatching { LocalDate.parse(it) }.getOrDefault(LocalDate(1900, 1, 1))
            } ?: LocalDate(1900, 1, 1)

            DialogLock(
                week = weeksInMonth,
                index = weekIndex,
                date = date,
                locked = locked,
                onAdd = {
                    // Lógica para bloquear la semana seleccionada o la anterior si corresponde
                    if (weeksInMonth[weekIndex].second > date) {
                        calendarBlockWeekViewModel.lockWeek(weeksInMonth[weekIndex])
                    } else {
                        if (weekIndex > 0) {
                            calendarBlockWeekViewModel.lockWeek(weeksInMonth[weekIndex - 1])
                        } else {
                            val previousWeek = calendarBlockWeekViewModel.getPreviousWeek(weeksInMonth[0])
                            calendarBlockWeekViewModel.lockWeek(previousWeek)
                        }
                    }
                    calendarBlockWeekViewModel.changeDialog(false)
                },
                onRemove = {
                    // Lógica para desbloquear la semana anterior
                    calendarBlockWeekViewModel.lockWeek(weeksInMonth[weekIndex - 1])
                    calendarBlockWeekViewModel.changeDialog(false)
                }
            ) { calendarBlockWeekViewModel.changeDialog(false) }
        }

        Column(Modifier.fillMaxSize().padding(top = 30.dp, end = 16.dp, start = 16.dp)) {
            // Encabezado de la pantalla con título y acción de añadir (sin acción aquí)
            HeaderSection(navigator, "Bloqueo Semanal", Icons.Filled.Add, false) { }

            // Controles para cambiar el mes (anterior y siguiente) y mostrar mes actual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarBlockWeekViewModel.onMonthChangePrevious(DatePeriod(months = 1))
                    }
                }) {
                    Text("<", fontSize = 24.sp)
                }

                Text(
                    "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { resetToday() }
                )

                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarBlockWeekViewModel.onMonthChangeFordward(DatePeriod(months = 1))
                    }
                }) {
                    Text(">", fontSize = 24.sp)
                }
            }

            // Lista de semanas con opción a bloquear/desbloquear y modificar empleados
            weeksInMonth.forEachIndexed { index, pair ->
                val generateBlock by calendarBlockWeekViewModel.blockDate.collectAsState()
                val date = generateBlock?.let {
                    runCatching { LocalDate.parse(it) }.getOrDefault(LocalDate(1900, 1, 1))
                } ?: LocalDate(1900, 1, 1)

                ElevatedCard(
                    colors = CardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().height(70.dp).padding(vertical = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Semana del ${pair.first.dayOfMonth} al ${pair.second.dayOfMonth}")

                        Row {
                            // Obtener icono y color según estado de bloqueo de la semana
                            val colors = calendarBlockWeekViewModel.getWeekColor(pair, employees)

                            // Botón para abrir diálogo de bloqueo/desbloqueo
                            IconButton(onClick = {
                                calendarBlockWeekViewModel.changeDialog(true)
                                calendarBlockWeekViewModel.changeWeekIndex(index)
                                calendarBlockWeekViewModel.changeEmployeesModifie(false)
                            }) {
                                Icon(
                                    imageVector = colors.first,
                                    contentDescription = "Bloquear/Desbloquear Semana",
                                    tint = colors.second
                                )
                            }

                            // Botón para modificar empleados de la semana
                            IconButton(onClick = {
                                if (employeeModifie && index == weekIndex)
                                    calendarBlockWeekViewModel.changeEmployeesModifie(false)
                                else
                                    calendarBlockWeekViewModel.changeEmployeesModifie(true)
                                calendarBlockWeekViewModel.changeWeekIndex(index)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Modificar Empleados",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // Sección para modificar bloqueo de empleados individualmente, si está activa
            if (employeeModifie) {
                ElevatedCard(
                    colors = CardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation(5.dp)
                ) {
                    Spacer(Modifier.size(20.dp))

                    var showFilter by remember { mutableStateOf(false) }
                    var changeDetected by remember { mutableStateOf(false) }
                    var orderDescendant by remember { mutableStateOf(false) }

                    // Fila con botón para filtro, título y ordenamiento
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton({ showFilter = !showFilter }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Filtrar")
                        }

                        Text(
                            "Semana del ${weeksInMonth[weekIndex].first.dayOfMonth} al ${weeksInMonth[weekIndex].second.dayOfMonth}",
                            fontWeight = FontWeight.Bold
                        )

                        IconButton({ orderDescendant = !orderDescendant }) {
                            Icon(
                                imageVector = if (orderDescendant) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                                contentDescription = "Ordenar"
                            )
                        }
                    }

                    // Campo de texto para filtro de empleados
                    if (showFilter) {
                        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                            OutlinedTextField(
                                value = filter,
                                onValueChange = { calendarBlockWeekViewModel.changeFilter(it) },
                                label = { Text("Filtro") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = customTextFieldColors()
                            )
                        }
                    }

                    // Lista filtrada y ordenada de empleados con checkbox para bloqueo
                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp).weight(6f)) {
                        val employeesFilter = if (filter.isNotBlank()) {
                            employees.filter {
                                val name = (it.nombre + " " + it.apellidos).lowercase()
                                filter.lowercase() in name
                            }
                        } else {
                            employees
                        }

                        items(employeesFilter.size) { employee ->
                            val employeesOrder = if (!orderDescendant)
                                employeesFilter.sortedByDescending { it.unblockDate }
                            else
                                employeesFilter.sortedBy { it.unblockDate }

                            val currentEmployee = employeesOrder[employee]

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${currentEmployee.nombre} ${currentEmployee.apellidos}")

                                val blockDate = currentEmployee.blockDate
                                val date = blockDate?.let {
                                    runCatching { LocalDate.parse(it) }.getOrDefault(LocalDate(2025, 1, 1))
                                } ?: LocalDate(2025, 1, 1)

                                val unblockDate = currentEmployee.unblockDate?.split("/")?.get(1)
                                val dateUnblock = unblockDate?.let { LocalDate.parse(it) }

                                Checkbox(
                                    checked = date >= weeksInMonth[weekIndex].second && weeksInMonth[weekIndex].second != dateUnblock,
                                    onCheckedChange = {
                                        val currentWeek = weeksInMonth[weekIndex]
                                        val employeeId = currentEmployee
                                        val shouldUnlock = date >= currentWeek.second && currentWeek.second != dateUnblock
                                        val targetWeek = if (shouldUnlock && weekIndex == 0) {
                                            calendarBlockWeekViewModel.getPreviousWeek(currentWeek)
                                        } else {
                                            currentWeek
                                        }

                                        calendarBlockWeekViewModel.lockWeekEmployee(targetWeek, employeeId, !shouldUnlock)

                                        changeDetected = true
                                    }
                                )
                            }
                        }
                    }

                    // Botón para guardar cambios si se detectaron modificaciones
                    if (changeDetected) {
                        Button({
                            calendarBlockWeekViewModel.lockWeekEmployees()
                            changeDetected = false
                        }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }


    /**
     * Composable que muestra un diálogo para confirmar bloqueo o desbloqueo de una semana.
     *
     * @param week Lista de pares de fechas que representan las semanas disponibles.
     * @param index Índice de la semana seleccionada en la lista.
     * @param date Fecha usada para comparar estado de bloqueo.
     * @param locked Lista de semanas bloqueadas.
     * @param onAdd Callback a ejecutar cuando se confirma el bloqueo.
     * @param onRemove Callback a ejecutar cuando se confirma el desbloqueo.
     * @param onDismiss Callback a ejecutar al cerrar el diálogo sin cambios.
     */
    @Composable
    fun DialogLock(
        week: List<Pair<LocalDate, LocalDate>>,
        index: Int,
        date: LocalDate,
        locked: List<Pair<LocalDate, LocalDate>>,
        onAdd: () -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)

                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = if (week[index].second > date) "¿Quieres bloquear esta fecha?" else "¿Quieres desbloquear esta fecha?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = onDismiss) {
                            Text("Cancelar")
                        }
                        Button(onClick = if (week[index] !in locked) onAdd else onRemove) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}