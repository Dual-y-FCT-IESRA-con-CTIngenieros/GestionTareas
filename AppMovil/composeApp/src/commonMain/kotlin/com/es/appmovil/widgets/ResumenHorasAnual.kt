package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResumenHorasAnual() {
    Text("Resumen anual")
    Row(
        Modifier.fillMaxWidth().shadow(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            Modifier.background(color = Color.Red).weight(0.9f).height(20.dp)
                .padding(end = 10.dp)
        )
        Box(
            Modifier.background(color = Color.Yellow).weight(0.5f).height(20.dp)
                .padding(end = 10.dp, start = 10.dp)
        )
        Box(
            Modifier.background(color = Color.Green).weight(0.9f).height(20.dp)
                .padding(start = 10.dp)
        )
    }
}