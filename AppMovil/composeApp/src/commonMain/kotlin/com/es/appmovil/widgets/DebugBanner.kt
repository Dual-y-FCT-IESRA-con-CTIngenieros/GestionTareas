package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Banner de depuración visible para confirmar que una pantalla ha sido cargada y los
 * cambios UI se han aplicado. Remover en producción.
 */
@Composable
fun DebugBanner(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB00020))
            .padding(6.dp)
    ) {
        Text(label, color = Color.White)
    }
}
