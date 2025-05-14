package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValuw
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.viewmodel.DataViewModel

class TableManageScreen : Screen {
    @Composable
    override fun Content() {

        LaunchedEffect(Unit){
            DataViewModel.load_tables()
        }

        val activities by DataViewModel.activities.collectAsState()
        val aircraft by DataViewModel.aircraft.collectAsState()
        val area by DataViewModel.area.collectAsState()
        val calendar by DataViewModel.calendar.collectAsState()
        val cliente by DataViewModel.cliente.collectAsState()
        val employees by DataViewModel.employees.collectAsState()
        val employeeWH by DataViewModel.employeeWH.collectAsState()
        val employeeActivities by DataViewModel.employeeActivities.collectAsState()
        val employeeWO by DataViewModel.employeeWO.collectAsState()
        val manager by DataViewModel.manager.collectAsState()
        val projects by DataViewModel.projects.collectAsState()
        val projectTimeCodes by DataViewModel.projectTimeCodes.collectAsState()
        val roles by DataViewModel.roles.collectAsState()
        val timeCodes by DataViewModel.timeCodes.collectAsState()
        val workOrders by DataViewModel.workOrders.collectAsState()

        var filters by remember { mutableStateOf(columns.associateWith { "" }) }
        var visibleRecords by remember { mutableStateOf(records.take(20)) }
        val listState = rememberLazyListState()


        LaunchedEffect(listState, filtered) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisible ->
                    if (lastVisible == visibleRecords.lastIndex && visibleRecords.size < filtered.size) {
                        visibleRecords = filtered.take(visibleRecords.size + 20)
                    }
                }
        }

        LazyColumn(state = listState) {
            items(loadedRecords) { }
        }
    }

    @Composable
    fun FilterBar(filters: Map<String, String>, onFilterChange: (String, String) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { (column, value) ->
                TextField(
                    value = value,
                    onValueChange = { onFilterChange(column, it) },
                    label = { Text("Filtrar $column") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    @Composable
    fun AdminScreen() {
        var filters by remember {
            mutableStateOf(mapOf("Nombre" to "", "Edad" to ""))
        }

        var allRecords by remember {
            mutableStateOf(
                listOf(
                    Record(1, mutableMapOf("Nombre" to "Juan", "Edad" to "25")),
                    Record(2, mutableMapOf("Nombre" to "Ana", "Edad" to "30"))
                    // más registros...
                )
            )
        }

        val filteredRecords = allRecords.filter { record ->
            filters.all { (col, filter) ->
                record.data[col]?.contains(filter, ignoreCase = true) ?: true
            }
        }

        Column {
            FilterBar(filters) { column, newValue ->
                filters = filters.toMutableMap().also { it[column] = newValue }
            }
            EditableTable(filteredRecords) { id, column, newValue ->
                allRecords = allRecords.map {
                    if (it.id == id) {
                        it.copy(
                            data = it.data.toMutableMap().also { map -> map[column] = newValue })
                    } else it
                }
            }
        }
    }

    @Composable
    fun EditableTable(records: List<Any>, onValueChange: (Int, String, String) -> Unit) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(records.size) { record ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    record.data.forEach { (column, value) ->
                        TextField(
                            value = value,
                            onValueChange = { newValue ->
                                onValueChange(
                                    record.id,
                                    column,
                                    newValue
                                )
                            },
                            label = { Text(column) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Button(onClick = { /* Guardar cambios */ }) {
                        Text("💾")
                    }
                }
            }
        }
    }


}