package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Period { WEEK, MONTH, YEAR }

@Composable
fun PeriodSelector(selected: Period, onSelect: (Period) -> Unit) {
    Row(modifier = Modifier.padding(8.dp)) {
        Button(onClick = { onSelect(Period.WEEK) }) { Text("Semana") }
        Button(onClick = { onSelect(Period.MONTH) }) { Text("Mes") }
        Button(onClick = { onSelect(Period.YEAR) }) { Text("Año") }
    }
}

