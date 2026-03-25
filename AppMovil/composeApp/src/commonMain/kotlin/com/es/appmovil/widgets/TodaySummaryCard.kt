package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Tarjeta resumen de hoy/semana (UI-only) con barras de progreso y estilo acorde al mockup.
 */
@Composable
fun TodaySummaryCard(
    todayHours: Float,
    weekHours: Float,
    dailyGoal: Float = 8f,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(4.dp)) {
        // Hoy
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Horas Hoy", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text("${todayHours}h", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0))) {
                    Box(modifier = Modifier
                        .fillMaxWidth((todayHours/dailyGoal).coerceIn(0f,1f))
                        .height(10.dp)
                        .background(Color(0xFFF4A900)))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("Meta: ${dailyGoal}h", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Semana
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Horas esta Semana", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text("${weekHours}h", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0))) {
                    Box(modifier = Modifier
                        .fillMaxWidth((weekHours/40f).coerceIn(0f,1f))
                        .height(10.dp)
                        .background(Color(0xFFF4A900)))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("Meta: 40h", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
