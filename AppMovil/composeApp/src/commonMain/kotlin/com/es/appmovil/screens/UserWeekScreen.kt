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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.model.Area
import com.es.appmovil.utils.ManageCSV
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.dailyHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.UserWeekViewModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class UserWeekScreen(private val userWeekViewModel: UserWeekViewModel) : Screen {

    @Composable
    override fun Content() {
        val manageCSV = ManageCSV()
        val area by DataViewModel.areas.collectAsState()
        var areaIndex by remember { mutableStateOf(0) }
        var selectedWeek by remember { mutableStateOf(1) }
        var expanded by remember { mutableStateOf(false) }

        var showDialog by remember { mutableStateOf(false) }
        var downloadWeekExpanded by remember { mutableStateOf(false) }

        val firstDayOfYear = LocalDate(today.value.year, 1, 1)
        val firstMonday = generateSequence(firstDayOfYear) { it.plus(DatePeriod(days = 1)) }
            .first { it.dayOfWeek == DayOfWeek.MONDAY }

        val firstDayOfWeek = firstMonday.plus(DatePeriod(days = (selectedWeek - 1) * 7))
        val lastDayOfWeek = firstDayOfWeek.plus(DatePeriod(days = 6))

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

            HeaderSection { showDialog = true }

            Spacer(modifier = Modifier.height(16.dp))

            WeekSelector(
                selectedWeek = selectedWeek,
                onWeekSelected = { selectedWeek = it },
                expanded = expanded,
                onExpandChange = { expanded = it },
                firstMonday = firstMonday
            )

            Spacer(modifier = Modifier.height(16.dp))

            AreaSelector(area = area, onAreaSelected = { areaIndex = it })

            Spacer(modifier = Modifier.height(16.dp))

            val employeeByArea = userWeekViewModel.getEmployeeByArea(
                area[areaIndex].desc,
                Pair(firstDayOfWeek, lastDayOfWeek)
            )

            EmployeeList(employeeByArea = employeeByArea)
        }
    }

    @Composable
    private fun HeaderSection(onDownloadClick: () -> Unit) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Control de Semanas", fontSize = 22.sp)
            IconButton(onClick = onDownloadClick) {
                Icon(imageVector = Icons.Filled.Download, contentDescription = "")
            }
        }
    }

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
            Text("Semana $selectedWeek", modifier = Modifier.clickable { onExpandChange(true) })

            Text(
                "${firstDay.dayOfMonth} ${firstDay.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} - " +
                        "${lastDay.dayOfMonth} ${lastDay.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }}"
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandChange(false) }) {
            (1..52).forEach { week ->
                val weekStart = firstMonday.plus(DatePeriod(days = (week - 1) * 7))
                val weekEnd = weekStart.plus(DatePeriod(days = 6))
                DropdownMenuItem(onClick = {
                    onWeekSelected(week)
                    onExpandChange(false)
                }) {
                    Text("Semana $week (${weekStart.dayOfMonth} ${weekStart.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} - " +
                            "${weekEnd.dayOfMonth} ${weekEnd.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }})")
                }
            }
        }
    }

    @Composable
    private fun AreaSelector(
        area: List<Area>,
        onAreaSelected: (Int) -> Unit
    ) {
        LazyRow(Modifier.fillMaxWidth()) {
            items(area.size) {
                if (area[it].idArea != 1)
                    Button(onClick = { onAreaSelected(it) }, colors = customButtonColors()) {
                        Text(area[it].desc.take(15))
                    }
                Spacer(Modifier.size(8.dp))
            }
        }
    }

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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExpandChange(true) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Semana $selectedWeek")
                        Text(
                            "${weekStart.dayOfMonth} ${weekStart.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} - " +
                                    "${weekEnd.dayOfMonth} ${weekEnd.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }}"
                        )
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { onExpandChange(false) }) {
                        (1..52).forEach { week ->
                            val start = firstMonday.plus(DatePeriod(days = (week - 1) * 7))
                            val end = start.plus(DatePeriod(days = 6))
                            DropdownMenuItem(onClick = {
                                onWeekSelected(week)
                                onExpandChange(false)
                            }) {
                                Text("Semana $week (${start.dayOfMonth} ${start.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} - " +
                                        "${end.dayOfMonth} ${end.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }})")
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