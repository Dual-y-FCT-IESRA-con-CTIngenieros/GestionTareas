package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.es.appmovil.viewmodel.ResumeViewmodel

/**
 * Composable que muestra un botón de información que despliega una leyenda con colores y textos.
 *
 * @param resumeViewmodel ViewModel que provee la leyenda como un mapa nombre-color.
 */
@Composable
fun LegendButton(resumeViewmodel: ResumeViewmodel) {
    var expanded by remember { mutableStateOf(false) }

    val colorMap = resumeViewmodel.getLegend()

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Legend")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Itera sobre cada entrada en la leyenda y crea un item con un círculo de color y texto
            colorMap.value.forEach { (name, color) ->
                DropdownMenuItem(onClick = {}) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(color), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name)
                    }
                }
            }
        }
    }
}
