package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(pageFunc: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize().padding(bottom = 20.dp), // ocupa toda la pantalla
        contentAlignment = Alignment.BottomCenter
    ) {
        FloatingActionButton(
            onClick = { pageFunc() },
            shape = CircleShape,
            contentColor = Color(0xFF7F57FF), // espacio respecto al borde inferior
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Menu")
        }
    }
}
