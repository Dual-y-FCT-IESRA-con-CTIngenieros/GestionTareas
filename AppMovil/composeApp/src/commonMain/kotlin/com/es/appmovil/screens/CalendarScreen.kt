package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.Calendar
import com.es.appmovil.widgets.ResumenHorasDia
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.TopBar

/**
 * Pantalla principal del calendario de actividades del empleado.
 * Muestra un calendario con las actividades del día y códigos de tiempo asociados.
 * Contiene además un resumen de horas del día y del mes.
 * Permite abrir diálogos para añadir o configurar actividades.
 */
class CalendarScreen : Screen {
    @Composable
    override fun Content() {
        // Navegador actual para controlar la navegación entre pantallas
        val navigator = LocalNavigator.currentOrThrow

        // ViewModels necesarios para la pantalla
        val calendarViewmodel = CalendarViewModel()
        val dayMenuViewModel = DayMenuViewModel()

        // Estado actual de la fecha
        val fechaActual by today.collectAsState()
        // Actividades del empleado para mostrar en el calendario
        val actividades by calendarViewmodel.employeeActivity.collectAsState()
        // Códigos de tiempo relacionados con las actividades
        val timeCodes by calendarViewmodel.timeCodes.collectAsState()

        // Estados para mostrar diálogos (agregar o configurar actividades)
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val showDialogConfig by calendarViewmodel.showDialogConfig.collectAsState()

        // Actualiza el pie de datos (puede ser para estadísticas o visualizaciones)
        DataViewModel.getPie()

        MaterialTheme {
            Scaffold(
                topBar = { TopBar(navigator, title = "Calendario") },
                bottomBar = { BottomNavigationBar(navigator) },
                floatingActionButton = {
                    ActionButton { calendarViewmodel.changeDialog(true) }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = false,
                backgroundColor = Color(0xFFF8F8F8) // Fondo suave
            ) { innerPadding ->
                Box(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(Color(0xFFF8F8F8))
                ) {
                    Column(Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
                        // Componente principal del calendario
                        Calendar(
                            calendarViewmodel,
                            dayMenuViewModel,
                            fechaActual,
                            showDialog,
                            showDialogConfig,
                            actividades,
                            timeCodes
                        )
                        Spacer(Modifier.size(16.dp))
                        // Fila con resumen de horas del día y resumen mensual
                        Row {
                            // Tarjeta con resumen de horas del día
                            ElevatedCard(
                                colors = CardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp, end = 4.dp, bottom = 20.dp)
                                    .shadow(8.dp, RoundedCornerShape(18.dp))
                                    .background(Color.White, RoundedCornerShape(18.dp))
                                    .clickable { calendarViewmodel.changeDialogConfig(true) },
                                elevation = CardDefaults.elevatedCardElevation(8.dp),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                ResumenHorasDia(calendarViewmodel)
                            }
                            Spacer(Modifier.size(12.dp))
                            // Columna con resumen mensual de horas
                            ElevatedCard(
                                colors = CardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp, bottom = 20.dp)
                                    .shadow(8.dp, RoundedCornerShape(18.dp))
                                    .background(Color.White, RoundedCornerShape(18.dp)),
                                elevation = CardDefaults.elevatedCardElevation(8.dp),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                ResumenHorasMensual()
                            }
                        }
                    }
                }
            }
        }
    }
}