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
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.CalendarManageViewModel
import com.es.appmovil.viewmodel.CalendarWeekViewModel
import com.es.appmovil.viewmodel.DataViewModel.employees
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.monthNameInSpanish
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate

class CalendarWeekScreen(private val calendarWeekViewModel: CalendarWeekViewModel) : Screen {
    @Composable
    override fun Content() {
        var monthChangeFlag = true
        val fechaActual by today.collectAsState()
        val showDialog by calendarWeekViewModel.showDialog.collectAsState()
        val weekIndex by calendarWeekViewModel.weekIndex.collectAsState()
        val locked by calendarWeekViewModel.locked.collectAsState()
        val employeeModifie by calendarWeekViewModel.employeeModifie.collectAsState()
        val weeksInMonth by calendarWeekViewModel.weeksInMonth.collectAsState()
        val employees by employees.collectAsState()
        val filter by calendarWeekViewModel.filter.collectAsState()
        calendarWeekViewModel.generateLock()


        if (showDialog) {
            val generateBlock by calendarWeekViewModel.blockDate.collectAsState()
            val date = generateBlock?.let {
                runCatching { LocalDate.parse(it) }.getOrDefault(
                    LocalDate(
                        1900,
                        1,
                        1
                    )
                )
            } ?: LocalDate(1900, 1, 1)

            DialogLock(
                week = weeksInMonth,
                index = weekIndex,
                date = date,
                locked = locked,
                onAdd = {
                    if (weeksInMonth[weekIndex].second > date) calendarWeekViewModel.lockWeek(
                        weeksInMonth[weekIndex]
                    )
                    else {
                        if (weekIndex > 0) calendarWeekViewModel.lockWeek(weeksInMonth[weekIndex - 1])
                        else {
                            val previousWeek = calendarWeekViewModel.getPreviousWeek(weeksInMonth[0])
                            calendarWeekViewModel.lockWeek(previousWeek)
                        }
                    }
                    calendarWeekViewModel.changeDialog(false)
                },
                onRemove = {
                    calendarWeekViewModel.lockWeek(weeksInMonth[weekIndex - 1])
                    calendarWeekViewModel.changeDialog(false)
                }) { calendarWeekViewModel.changeDialog(false) }
        }

        Column(Modifier.fillMaxSize().padding(top = 30.dp, end = 16.dp, start = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarWeekViewModel.onMonthChangePrevious(DatePeriod(months = 1))
                    }
                }) {
                    Text("<", fontSize = 24.sp)
                }
                Text(
                    "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { resetToday() })
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarWeekViewModel.onMonthChangeFordward(DatePeriod(months = 1))
                    }
                }) {
                    Text(">", fontSize = 24.sp)
                }
            }

            weeksInMonth.forEachIndexed { index, pair ->
                val generateBlock by calendarWeekViewModel.blockDate.collectAsState()
                val date = generateBlock?.let {
                    runCatching { LocalDate.parse(it) }.getOrDefault(
                        LocalDate(
                            1900,
                            1,
                            1
                        )
                    )
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Semana del ${pair.first.dayOfMonth} al ${pair.second.dayOfMonth}")
                        Row {
                            val colors = calendarWeekViewModel.getWeekColor(pair, employees)
                            IconButton(onClick = {
                                calendarWeekViewModel.changeDialog(true)
                                calendarWeekViewModel.changeWeekIndex(index)
                                calendarWeekViewModel.changeEmployeesModifie(false)
                            }) {
                                if (weeksInMonth[index].second <= date) Icon(
                                    imageVector = colors.first,
                                    contentDescription = "",
                                    tint = colors.second
                                )
                                else Icon(
                                    imageVector = colors.first,
                                    contentDescription = "",
                                    tint = colors.second
                                )
                            }
                            IconButton(onClick = {
                                if (employeeModifie && index == weekIndex) calendarWeekViewModel.changeEmployeesModifie(
                                    false
                                ) else calendarWeekViewModel.changeEmployeesModifie(true)
                                calendarWeekViewModel.changeWeekIndex(index)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp).weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton({ showFilter = !showFilter }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Filtrar"
                            )
                        }
                        Text(
                            "Semana del ${weeksInMonth[weekIndex].first.dayOfMonth} al ${weeksInMonth[weekIndex].second.dayOfMonth}",
                            fontWeight = FontWeight.Bold
                        )
                        IconButton({ orderDescendant = !orderDescendant }) {
                            Icon(
                                imageVector = if (orderDescendant) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                                contentDescription = "Filtrar"
                            )
                        }

                    }
                    if (showFilter) {
                        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                            OutlinedTextField(
                                value = filter,
                                onValueChange = { calendarWeekViewModel.changeFilter(it) },
                                label = { Text("Filtro") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = customTextFieldColors()
                            )
                        }
                    }

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
                            val employeesOrder = if (!orderDescendant) employeesFilter.sortedByDescending { it.unblockDate } else employeesFilter.sortedBy { it.unblockDate }
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${employeesOrder[employee].nombre} ${employeesOrder[employee].apellidos}"

                                )
                                val blockDate = employeesOrder[employee].blockDate
                                val date = blockDate?.let {
                                    runCatching { LocalDate.parse(it) }.getOrDefault(
                                        LocalDate(
                                            2025,
                                            1,
                                            1
                                        )
                                    )
                                } ?: LocalDate(2025, 1, 1)

                                val unblockDate = employeesOrder[employee].unblockDate?.split("/")?.get(1)
                                val dateUnblock = unblockDate?.let { LocalDate.parse(it) }

                                Checkbox(
                                    checked = date >= weeksInMonth[weekIndex].second && weeksInMonth[weekIndex].second != dateUnblock,
                                    onCheckedChange = {
                                        val currentWeek = weeksInMonth[weekIndex]
                                        val employeeId = employeesOrder[employee]

                                        val shouldUnlock = date >= currentWeek.second && currentWeek.second != dateUnblock

                                        val targetWeek = if (shouldUnlock && weekIndex == 0) {
                                            calendarWeekViewModel.getPreviousWeek(currentWeek)
                                        } else {
                                            currentWeek
                                        }

                                        calendarWeekViewModel.lockWeekEmployee(
                                            targetWeek,
                                            employeeId,
                                            !shouldUnlock
                                        )

                                        changeDetected = true
                                    }
                                )
                            }
                        }
                    }

                    if (changeDetected) {
                        Button({
                            calendarWeekViewModel.lockWeekEmployees()
                            changeDetected = false
                        }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DialogLock(
        week: List<Pair<LocalDate, LocalDate>>,
        index: Int,
        date:LocalDate,
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