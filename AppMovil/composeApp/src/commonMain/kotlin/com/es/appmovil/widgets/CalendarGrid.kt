package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Grid de calendario simple (UI-only). No maneja persistencia; recibe un mapa de dias con valores.
 * Implementación simplificada que usa keys String (YYYY-MM-DD) para evitar dependencias de tipo de fecha.
 */
@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    entries: Map<String, Float> = emptyMap(),
    selectedDayKey: String? = null,
    onDaySelected: (String) -> Unit = {}
) {
    // Para simplicidad mostramos 1..28 días y evitamos java.time/kotlinx en la interfaz pública
    val daysInMonth = remember(year, month) { 28 }
    val rows = remember(daysInMonth) { (daysInMonth + 6) / 7 }

    Column(modifier = Modifier.fillMaxWidth()) {
        for (r in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                for (c in 0 until 7) {
                    val index = r * 7 + c
                    if (index < daysInMonth) {
                        val day = index + 1
                        val dayKey = "%04d-%02d-%02d".format(year, month, day.coerceAtLeast(1))
                        val hasEntry = entries.containsKey(dayKey)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { onDaySelected(dayKey) }
                                .background(if (selectedDayKey == dayKey) MaterialTheme.colorScheme.primary else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = day.toString(), style = MaterialTheme.typography.bodySmall)
                                if (hasEntry) {
                                    Text(text = "•", color = Color.Green)
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.size(40.dp)) {}
                    }
                }
            }
        }
    }
}
