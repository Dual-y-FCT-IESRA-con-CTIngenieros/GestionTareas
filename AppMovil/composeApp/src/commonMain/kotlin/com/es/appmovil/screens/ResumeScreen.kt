package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.viewmodel.DataViewModel.dailyHours
import com.es.appmovil.viewmodel.DataViewModel.getHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.viewmodel.ResumeViewmodel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.ConteoHoras
import com.es.appmovil.widgets.DayDialog
import com.es.appmovil.widgets.LegendButton
import com.es.appmovil.widgets.ResumenHorasAnual
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.TopBar

/**
 * Pantalla principal de resumen donde se muestran distintas vistas relacionadas
 * con las horas trabajadas, resumen semanal, mensual y anual, además de un botón
 * para acceder a la administración si el usuario tiene el rol adecuado.
 *
 * Esta pantalla utiliza múltiples ViewModels para gestionar los datos y la navegación.
 */
class ResumeScreen : Screen {
    /**
     * Composable principal que monta la UI de la pantalla de resumen.
     *
     * Muestra el título, leyenda, resumen semanal, conteo de horas, resumen anual,
     * resumen mensual y un botón de administración para usuarios con rol específico.
     * También incluye una barra inferior de navegación y un botón flotante para abrir
     * un diálogo de calendario.
     *
     * Utiliza [Scaffold] para estructurar la pantalla y maneja la navegación interna
     * mediante 'Navigator'.
     */
    @Composable
    override fun Content() {
        val resumeViewmodel = ResumeViewmodel()
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow
        var canClick by remember { mutableStateOf(true) }
        val currentHours by currentHours.collectAsState()
        val calendarViewmodel = CalendarViewModel()
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val dayMenuViewModel = DayMenuViewModel()
        val dailyHours by dailyHours.collectAsState()
        val currentDay by resumeViewmodel.currentDay.collectAsState()

        MaterialTheme {
            Scaffold(
                topBar = {
                    TopBar(navigator, title = "Resumen", rightContent = { LegendButton(resumeViewmodel) })
                },
                bottomBar = {
                    BottomNavigationBar(navigator)
                },
                floatingActionButton = { ActionButton { calendarViewmodel.changeDialog(true) } },
                floatingActionButtonPosition = FabPosition.Center, // o End
                isFloatingActionButtonDocked = false
            ) {
                getHours()
                Column(Modifier.padding(16.dp)) {
                    // Mostrar solo el texto de día y semana en formato natural
                    val fecha = today.value
                    val nombreDia = when (fecha.dayOfWeek.ordinal) {
                        0 -> "Lunes"
                        1 -> "Martes"
                        2 -> "Miércoles"
                        3 -> "Jueves"
                        4 -> "Viernes"
                        5 -> "Sábado"
                        6 -> "Domingo"
                        else -> fecha.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
                    }
                    val nombreMes = when (fecha.monthNumber) {
                        1 -> "enero"
                        2 -> "febrero"
                        3 -> "marzo"
                        4 -> "abril"
                        5 -> "mayo"
                        6 -> "junio"
                        7 -> "julio"
                        8 -> "agosto"
                        9 -> "septiembre"
                        10 -> "octubre"
                        11 -> "noviembre"
                        12 -> "diciembre"
                        else -> fecha.month.name.lowercase().replaceFirstChar { it.uppercase() }
                    }
                    Text(
                        text = "$nombreDia ${fecha.dayOfMonth} de $nombreMes de ${fecha.year}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            ConteoHoras(currentHours, dailyHours, currentDay)
                            Spacer(Modifier.size(20.dp))
                            Column(Modifier.clickable {
                                if (canClick) {
                                    canClick = false
                                    navigator.push(AnualScreen())
                                }
                            }) {
                                ResumenHorasAnual(resumeViewmodel)
                            }
                        }
                        Spacer(Modifier.size(16.dp))
                        Column(Modifier.weight(1f).clickable {
                            if (canClick) {
                                canClick = false
                                navigator.push(CalendarScreen())
                            }
                        }) {
                            Row {
                                Text("Resumen mensual", fontWeight = FontWeight.SemiBold)
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "ArrowForward"
                                )
                            }
                            Spacer(Modifier.size(20.dp))
                            ResumenHorasMensual()
                        }
                    }
                    Spacer(Modifier.size(40.dp))

                    // Eliminado el botón de administración y la lógica de rol de administrador
                }
                DayDialog(showDialog, today.value, dayMenuViewModel, calendarViewmodel) {
                    calendarViewmodel.changeDialog(it)
                }
            }
        }
    }
}
