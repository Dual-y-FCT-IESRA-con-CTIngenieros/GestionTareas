package com.es.appmovil.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.viewmodel.DataViewModel.dailyHours
import com.es.appmovil.viewmodel.DataViewModel.getHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.DayDialog
import com.es.appmovil.widgets.TopBar
import com.es.appmovil.widgets.TodaySummaryCard
import com.es.appmovil.widgets.ProgressThermometer

/**
 * Pantalla principal de resumen donde se muestran distintas vistas relacionadas
 * con las horas trabajadas, resumen semanal, mensual y anual, además de un botón
 * para acceder a la administración si el usuario tiene el rol adecuado.
 */
class HomeScreen : Screen {
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
        val navigator = LocalNavigator.currentOrThrow
        val currentHours by currentHours.collectAsState()
        val calendarViewmodel = CalendarViewModel()
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val dayMenuViewModel = DayMenuViewModel()
        val dailyHours by dailyHours.collectAsState()

        MaterialTheme {
            Scaffold(
                topBar = { TopBar(navigator, title = "", rightContent = null) },
                bottomBar = { BottomNavigationBar(navigator) }
            ) { innerPadding ->
                getHours()
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .padding(16.dp)
                        .padding(bottom = 88.dp) // evita que el contenido quede oculto por la barra inferior
                ) {
                    // Fecha pequeña
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
                        1 -> "enero"; 2 -> "febrero"; 3 -> "marzo"; 4 -> "abril"; 5 -> "mayo"; 6 -> "junio"
                        7 -> "julio"; 8 -> "agosto"; 9 -> "septiembre"; 10 -> "octubre"; 11 -> "noviembre"; 12 -> "diciembre"
                        else -> fecha.month.name.lowercase().replaceFirstChar { it.uppercase() }
                    }
                    Text(text = "${nombreDia}, ${fecha.dayOfMonth} De ${nombreMes.replaceFirstChar { it.uppercase() }}", color = Color.Gray)

                    Spacer(Modifier.size(8.dp))

                    // Título grande como en el mockup
                    Text(text = "Seguimiento de\nHoras", style = MaterialTheme.typography.displayLarge, color = Color(0xFFF4A900))

                    Spacer(Modifier.size(16.dp))

                    // Card principal con termómetro
                    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            ProgressThermometer(currentHours = currentHours.toFloat(), targetHours = (dailyHours * 20).toFloat(), onRegisterClick = { calendarViewmodel.changeDialog(true) })
                        }
                    }

                    Spacer(Modifier.size(16.dp))

                    // Tarjetas de hoy/semana
                    TodaySummaryCard(todayHours = currentHours.toFloat(), weekHours = (currentHours + 10).toFloat())

                    Spacer(Modifier.size(12.dp))

                    // Registros recientes
                    Text(text = "Registros Recientes", style = MaterialTheme.typography.titleMedium)
                    Box(modifier = Modifier.height(200.dp)) {
                        com.es.appmovil.widgets.RecentRecordsList(records = listOf(
                            com.es.appmovil.widgets.RecordUi("1", "Hoy", "09:00 - 17:30"),
                            com.es.appmovil.widgets.RecordUi("2", "Ayer", "09:00 - 18:00"),
                            com.es.appmovil.widgets.RecordUi("3", "23 Mar", "09:00 - 17:30")
                        ))
                    }
                }
                DayDialog(showDialog, today.value, dayMenuViewModel, calendarViewmodel) {
                    calendarViewmodel.changeDialog(it)
                }
            }
        }
    }
}
