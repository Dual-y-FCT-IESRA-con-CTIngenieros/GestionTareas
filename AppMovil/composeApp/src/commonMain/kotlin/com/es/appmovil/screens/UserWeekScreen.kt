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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.UserWeekViewModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class UserWeekScreen(private val userWeekViewModel: UserWeekViewModel):Screen {
    @Composable
    override fun Content() {
        val area by DataViewModel.areas.collectAsState()
        var areaIndex  by remember { mutableStateOf(0) }
        var selectedWeek by remember { mutableStateOf(1) }
        var expanded by remember { mutableStateOf(false) }

        val firstDayOfYear = LocalDate(today.value.year, 1, 1)
        val firstMonday = generateSequence(firstDayOfYear) { it.plus(DatePeriod(days = 1)) }
            .first { it.dayOfWeek == DayOfWeek.MONDAY }

        val firstDayOfWeek = firstMonday.plus(DatePeriod(days = (selectedWeek - 1) * 7))
        val lastDayOfWeek = firstDayOfWeek.plus(DatePeriod(days = 6))


        Column(Modifier.fillMaxSize().padding(16.dp)){
            Text("Áreas")
            LazyRow(Modifier.fillMaxWidth()) {
                items(area.size) {
                    if(area[it].idArea != 1)
                        Button(onClick = { areaIndex = it }) {
                            Text(area[it].desc.take(15))
                        }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Semana $selectedWeek", modifier = Modifier.clickable { expanded = true })

                Spacer(modifier = Modifier.width(16.dp))
                Text("${firstDayOfWeek.dayOfMonth} ${firstDayOfWeek.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} - ${lastDayOfWeek.dayOfMonth} ${lastDayOfWeek.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }}")

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    (1..52).forEach { week ->
                        DropdownMenuItem(onClick = {
                            selectedWeek = week
                            expanded = false
                        }) {
                            Text("Semana $week")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val employeeByArea = userWeekViewModel.getEmployeeByArea(
                area[areaIndex].desc,
                Pair(firstDayOfWeek, lastDayOfWeek)
            )

            LazyColumn {
                item { employeeByArea.entries.toList().forEach { (name, hours) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(name)
                        Text("$hours hrs")
                    }
                }
                }

            }
        }
    }
}