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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.FreeCancellation
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarBlockWeekViewModel
import com.es.appmovil.viewmodel.CalendarFestViewModel
import com.es.appmovil.viewmodel.CalendarYearViewModel
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.UserWeekViewModel
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.HeaderSection

/**
 * Pantalla principal para la gestión del calendario.
 * Permite navegar a diferentes secciones para:
 * - Bloqueo semanal de calendarios.
 * - Gestión anual de calendario.
 * - Gestión de horas semanales de usuario.
 * - Calendario de días festivos.
 *
 * Utiliza un Scaffold con barra inferior de navegación y tarjetas para seleccionar cada opción.
 */
class CalendarManageScreen : Screen {
    @Composable
    override fun Content() {
        // Tamaño de iconos en las tarjetas
        val size = 90
        // Flag para evitar múltiples clics rápidos y navegación duplicada
        var canClick by remember { mutableStateOf(true) }
        val navigator = LocalNavigator.currentOrThrow

        // Instancias de ViewModel para pasar a cada pantalla de gestión
        val calendarBlockWeekViewModel = CalendarBlockWeekViewModel()
        val calendarYearViewModel = CalendarYearViewModel()
        val userWeekViewModel = UserWeekViewModel()
        val calendarFestViewModel = CalendarFestViewModel()

        MaterialTheme {
            Scaffold(
                bottomBar = { BottomNavigationBar(navigator) }
            ) {
                // Reseteamos estado de día actual y semanas al iniciar
                resetToday()
                calendarBlockWeekViewModel.resetWeeks()

                Column(
                    Modifier.fillMaxSize()
                        .padding(top = 30.dp, start = 16.dp, end = 16.dp)
                ) {
                    HeaderSection(
                        navigator,
                        "Gestión De Calendario",
                        Icons.Filled.Download,
                        false
                    ) {}

                    Spacer(Modifier.size(30.dp))

                    // Primera fila: Bloqueo semanal y Gestión anual
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tarjeta para bloquear semanas
                        ElevatedCard(
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(CalendarBlockWeekScreen(calendarBlockWeekViewModel))
                                    canClick = false
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Filled.FreeCancellation, contentDescription = "Semanal", modifier = Modifier.size(size.dp), tint = Color(0xFF707272))
                                Text("Bloqueo Semanal")
                            }
                        }

                        Spacer(Modifier.size(16.dp))

                        // Tarjeta para gestión anual
                        ElevatedCard(
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(CalendarYearScreen(calendarYearViewModel))
                                    canClick = false
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Filled.EventBusy, contentDescription = "Anual", modifier = Modifier.size(size.dp), tint = Color(0xFF707272))
                                Text("Gestión Anual")
                            }
                        }
                    }

                    Spacer(Modifier.size(16.dp))

                    // Segunda fila: Horas semanales y Calendario festivos
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tarjeta para gestión de horas semanales
                        ElevatedCard(
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(UserWeekScreen(userWeekViewModel))
                                    canClick = false
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = "Horas Semanales", modifier = Modifier.size(size.dp), tint = Color(0xFF707272))
                                Text("Horas Semanales")
                            }
                        }

                        Spacer(Modifier.size(16.dp))

                        // Tarjeta para calendario festivos
                        ElevatedCard(
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(CalendarFestScreen(calendarFestViewModel))
                                    canClick = false
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Filled.EditCalendar, contentDescription = "Calendario Festivos", modifier = Modifier.size(size.dp), tint = Color(0xFF707272))
                                Text("Calendario Festivos")
                            }
                        }
                    }
                }
            }
        }
    }
}