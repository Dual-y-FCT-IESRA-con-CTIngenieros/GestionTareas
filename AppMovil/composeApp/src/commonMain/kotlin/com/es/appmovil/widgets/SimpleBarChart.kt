package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class BarItem(val label: String, val value: Float)

@Composable
fun SimpleBarChart(items: List<BarItem>, maxValue: Float = items.maxOfOrNull { it.value } ?: 1f) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        items.forEach { item ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(item.label, style = MaterialTheme.typography.bodyMedium)
                val widthPercent = (item.value / maxValue).coerceIn(0f, 1f)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.LightGray)) {
                    Box(modifier = Modifier
                        .fillMaxWidth(widthPercent)
                        .height(16.dp)
                        .background(MaterialTheme.colorScheme.primary))
                }
            }
        }
    }
}
