package com.es.appmovil.widgets

import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pequeño toggle UI-only para cambiar tema visualmente (controlado por el padre).
 */
@Composable
fun ThemeToggle(isDark: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = if (isDark) "Dark" else "Light", style = MaterialTheme.typography.bodyMedium)
        Switch(checked = isDark, onCheckedChange = { onToggle(it) })
    }
}
