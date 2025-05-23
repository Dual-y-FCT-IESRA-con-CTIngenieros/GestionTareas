package com.es.appmovil.widgets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ActionButton(pageFunc: () -> Unit) {
    FloatingActionButton(
        onClick = { pageFunc() },
        shape = CircleShape,
        backgroundColor = Color(0xFFF4A900), // espacio respecto al borde inferior
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Menu", tint = Color.Black)
    }
}
