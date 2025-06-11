package com.es.appmovil.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.database.Database
import com.es.appmovil.interfaces.TableEntry
import com.es.appmovil.model.Activity
import com.es.appmovil.model.Aircraft
import com.es.appmovil.model.Area
import com.es.appmovil.model.Client
import com.es.appmovil.model.Manager
import com.es.appmovil.model.Project
import com.es.appmovil.model.Rol
import com.es.appmovil.model.TimeCode
import com.es.appmovil.model.WorkOrder
import com.es.appmovil.utils.ManageCSV
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.utils.customTextFieldColors
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.FullScreenLoadingManager
import com.es.appmovil.widgets.HeaderSection
import com.es.appmovil.widgets.claseTabla
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

/**
 * Clase principal para la pantalla de gestión de tablas.
 * Implementa una pantalla que permite visualizar, editar y generar CSVs de diversas tablas.
 */
class TableManageScreen : Screen {

    /**
     * Composable principal que representa el contenido de la pantalla.
     *
     * - Carga los datos de las tablas al iniciarse.
     * - Muestra un listado con los nombres de las tablas.
     * - Permite editar o agregar entradas en cada tabla.
     * - Permite abrir un diálogo para exportar la tabla seleccionada en formato CSV.
     */
    @Composable
    override fun Content() {

        val navigator: Navigator = LocalNavigator.currentOrThrow
        var showDialog by remember { mutableStateOf(false) }

        // Carga las tablas y muestra/oculta loader
        LaunchedEffect(Unit) {
            FullScreenLoadingManager.showLoader()
            DataViewModel.load_tables()
            FullScreenLoadingManager.hideLoader()
        }

        // Mapa mutable que almacenará los datos de cada tabla por su nombre
        val tables = mutableMapOf<String, List<Any>>()

        // Colección de estados para las distintas tablas del ViewModel
        val tablesData = listOf(
            DataViewModel.activities.collectAsState(),
            DataViewModel.aircraft.collectAsState(),
            DataViewModel.areas.collectAsState(),
            DataViewModel.cliente.collectAsState(),
            DataViewModel.manager.collectAsState(),
            DataViewModel.projects.collectAsState(),
            DataViewModel.roles.collectAsState(),
            DataViewModel.timeCodes.collectAsState(),
            DataViewModel.workOrders.collectAsState(),
        )
        val tablesNames by DataViewModel.tablesNames.collectAsState()

        // Asociar cada nombre de tabla con sus datos correspondientes
        tablesNames.forEachIndexed() { index, tableName ->
            tables[tableName] = tablesData[index].value
        }

        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
            // Cabecera con título, botón para descarga CSV y navegación
            HeaderSection(navigator, "Tablas", Icons.Filled.Download, true) { showDialog = true }

            // Listado de tablas con botón para editar y añadir entradas
            LazyColumn {
                tables.forEach { (tableName, tableData) ->
                    item {
                        TableItem(
                            tableName
                        ) {
                            val tableEntries: List<TableEntry> =
                                tableData.filterIsInstance<TableEntry>()
                            navigator.push(
                                TableManageDataScreen(
                                    tableName,
                                    tableEntries
                                ) { _, data ->
                                    val tableEdit = reconstructEntry(tableName, data)
                                    updateTable(tableName, tableEdit.getFieldMap())
                                }
                            )
                        }
                    }

                }
            }
        }
        // Diálogo para exportar tablas a CSV
        csvDialog(showDialog, tablesNames) { showDialog = false }
    }

    /**
     * Composable que representa un ítem de tabla en la lista.
     *
     * @param tableName Nombre de la tabla.
     * @param onEditClick Lambda que se ejecuta al pulsar el botón de editar.
     */
    @Composable
    fun TableItem(
        tableName: String,
        onEditClick: () -> Unit
    ) {

        var showEditor by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tableName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showEditor = !showEditor }) {
                        if (!showEditor) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Agregar")
                        } else {
                            Icon(Icons.Default.Remove, contentDescription = "Menos")
                        }
                    }
                }
            }
            // Muestra un editor para agregar datos cuando showEditor es true
            if (showEditor) {
                claseTabla(tableName) // Función para mostrar formulario de edición/agregado
            }
        }
    }

    /**
     * Composable que muestra un diálogo para seleccionar una tabla y generar su CSV.
     *
     * @param showDialog Controla si el diálogo está visible.
     * @param tables Lista con los nombres de las tablas disponibles.
     * @param onDismiss Lambda para cerrar el diálogo.
     */
    @Composable
    fun csvDialog(showDialog: Boolean, tables: List<String>, onDismiss: () -> Unit) {

        var tableSelected by remember { mutableStateOf("") }

        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss,
            ) {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Generar CSV de la tabla",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¿De qué tabla quieres generar el csv?",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        TableSelector(
                            tables,
                            tableSelected,
                            onTableSelection = { tableSelected = it })

                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = onDismiss,
                                colors = customButtonColors(),
                            ) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = {
                                    ManageCSV().generateCSV(tableSelected)
                                    onDismiss.invoke()
                                },
                                colors = customButtonColors(),
                            ) {
                                Text("Aceptar")
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Composable que muestra un Dropdown para seleccionar una tabla.
     *
     * @param tablesNames Lista con los nombres de las tablas.
     * @param tableSelected Tabla actualmente seleccionada.
     * @param onTableSelection Callback para actualizar la tabla seleccionada.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TableSelector(
        tablesNames: List<String>,
        tableSelected: String,
        onTableSelection: (String) -> Unit,
    ) {
        var expandirTablas by remember { mutableStateOf(false) }
        // Dropdown de Tablas
        ExposedDropdownMenuBox(
            expanded = expandirTablas,
            onExpandedChange = { expandirTablas = !expandirTablas },
            modifier = Modifier.padding(end = 16.dp, start = 16.dp)
        ) {
            OutlinedTextField(
                colors = customTextFieldColors(),
                value = tableSelected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione Tabla") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTablas) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandirTablas,
                onDismissRequest = { expandirTablas = false }
            ) {
                tablesNames
                    .forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                onTableSelection(it)
                                expandirTablas = false
                            }
                        )
                    }
            }
        }
    }

    /**
     * Actualiza la tabla especificada con los datos recibidos.
     * Ejecuta la actualización en un hilo de background y maneja el loader de pantalla completa.
     *
     * @param tableName Nombre de la tabla a actualizar.
     * @param data Mapa con los campos y valores para la actualización.
     */
    private fun updateTable(tableName: String, data: Map<String, Any?>) {
        CoroutineScope(Dispatchers.IO).launch {
            FullScreenLoadingManager.showLoader()
            when (tableName) {
                "Activity" -> {
                    val activity = Activity(
                        idActivity = data["idActivity"] as Int,
                        idTimeCode = data["idTimeCode"] as Int,
                        desc = data["desc"] as String,
                        dateFrom = data["dateFrom"] as String?,
                        dateTo = data["dateTo"] as String?
                    )
                    Database.updateData<Activity>(tableName, activity)
                }

                "Aircraft" -> {
                    val aircraft = Aircraft(
                        idAircraft = data["idAircraft"] as String,
                        desc = data["desc"] as String
                    )
                    Database.updateData<Aircraft>(tableName, aircraft)
                }

                "Area" -> {
                    val area = Area(
                        idArea = data["idArea"] as Int,
                        desc = data["desc"] as String
                    )
                    Database.updateData<Area>(tableName, area)
                }

                "Client" -> {
                    val client = Client(
                        idCliente = data["idCliente"] as Int,
                        nombre = data["nombre"] as String
                    )
                    Database.updateData<Client>(tableName, client)
                }

                "Manager" -> {
                    val manager = Manager(
                        idManager = data["idManager"] as Int,
                        nombre = data["nombre"] as String,
                        apellidos = data["apellidos"] as String
                    )
                    Database.updateData<Manager>(tableName, manager)
                }

                "Project" -> {
                    val project = Project(
                        idProject = data["idProject"] as String,
                        desc = data["desc"] as String,
                        idCliente = data["idCliente"] as Int?
                    )
                    Database.updateData<Project>(tableName, project)
                }

                "Rol" -> {
                    val rol = Rol(
                        idRol = data["idRol"] as Int,
                        rol = data["rol"] as String
                    )
                    Database.updateData<Rol>(tableName, rol)
                }

                "TimeCode" -> {
                    val timeCode = TimeCode(
                        idTimeCode = data["idTimeCode"] as Int,
                        desc = data["desc"] as String,
                        color = data["color"] as String,
                        chkProd = data["chkProd"] as Boolean
                    )
                    Database.updateData<TimeCode>(tableName, timeCode)
                }

                "WorkOrder" -> {
                    val workOrder = WorkOrder(
                        idWorkOrder = data["idWorkOrder"] as String,
                        desc = data["desc"] as String,
                        projectManager = data["projectManager"] as Int?,
                        idProject = data["idProject"] as String,
                        idAircraft = data["idAircraft"] as Int?,
                        idArea = data["idArea"] as Int?
                    )
                    Database.updateData<WorkOrder>(tableName, workOrder)
                }

                else -> error("Tipo de tabla desconocido: $tableName")
            }
            FullScreenLoadingManager.hideLoader()
        }
    }

    /**
     * Reconstruye una instancia de TableEntry a partir de un mapa de datos y el nombre de la tabla.
     *
     * @param tableName Nombre de la tabla.
     * @param data Mapa con los campos y valores de la tabla.
     * @return Una instancia específica de TableEntry con los datos recibidos.
     * @throws IllegalArgumentException si el nombre de la tabla no coincide con ninguna conocida.
     */
    private fun reconstructEntry(tableName: String, data: Map<String, Any?>): TableEntry {
        return when (tableName) {
            "Activity" -> {
                Activity(
                    idActivity = data["idActivity"] as Int,
                    idTimeCode = data["idTimeCode"] as Int,
                    desc = data["desc"] as String,
                    dateFrom = data["dateFrom"] as String?,
                    dateTo = data["dateTo"] as String?
                )

            }

            "Aircraft" -> Aircraft(
                idAircraft = data["idAircraft"] as String,
                desc = data["desc"] as String
            )

            "Area" -> Area(
                idArea = data["idArea"] as Int,
                desc = data["desc"] as String
            )

            "Client" -> Client(
                idCliente = data["idCliente"] as Int,
                nombre = data["nombre"] as String
            )

            "Manager" -> Manager(
                idManager = data["idManager"] as Int,
                nombre = data["nombre"] as String,
                apellidos = data["apellidos"] as String
            )

            "Project" -> Project(
                idProject = data["idProject"] as String,
                desc = data["desc"] as String,
                idCliente = data["idCliente"].toString().toIntOrNull()
            )

            "Rol" -> Rol(
                idRol = data["idRol"] as Int,
                rol = data["rol"] as String
            )

            "TimeCode" -> TimeCode(
                idTimeCode = data["idTimeCode"] as Int,
                desc = data["desc"] as String,
                color = data["color"] as String,
                chkProd = data["chkProd"] as Boolean
            )

            "WorkOrder" -> WorkOrder(
                idWorkOrder = data["idWorkOrder"] as String,
                desc = data["desc"] as String,
                projectManager = data["projectManager"].toString().toIntOrNull(),
                idProject = data["idProject"] as String,
                idAircraft = data["idAircraft"].toString().toIntOrNull(),
                idArea = data["idArea"].toString().toIntOrNull()
            )

            else -> error("Tipo de tabla desconocido: $tableName")
        }
    }
}