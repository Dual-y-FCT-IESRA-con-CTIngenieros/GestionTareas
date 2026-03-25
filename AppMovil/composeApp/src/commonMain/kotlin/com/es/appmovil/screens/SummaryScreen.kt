package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.widgets.ProgressThermometer
import com.es.appmovil.widgets.TodaySummaryCard
import com.es.appmovil.widgets.CalendarGrid

/**
 * Pantalla UI-only de ejemplo que refleja partes del mockup: termómetro, tarjeta de hoy y calendario.
 * No toca ViewModels/DB; usa datos de ejemplo o los datos proporcionados por pantallas superiores.
 */
class SummaryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        MaterialTheme {
            Scaffold(bottomBar = { /* Reusar BottomNavigationBar si se desea */ }) { innerPadding ->
                Column(Modifier.fillMaxWidth().padding(innerPadding).padding(16.dp)) {
                    Text("Resumen (mockup)", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(12.dp))
                    ProgressThermometer(currentHours = 32f, targetHours = 160f, onRegisterClick = {})
                    Spacer(Modifier.height(12.dp))
                    TodaySummaryCard(todayHours = 8f, weekHours = 40f)
                    Spacer(Modifier.height(12.dp))
                    CalendarGrid(year = 2026, month = 3)
                }
            }
        }
    }
}
