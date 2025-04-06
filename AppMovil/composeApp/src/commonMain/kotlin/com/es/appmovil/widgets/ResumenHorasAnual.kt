package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResumenHorasAnual() {
    // Creamos la variable de modificador base de los box
    val boxModifier = Modifier.height(20.dp).clip(shape = RoundedCornerShape(5.dp))

    Row {
        Text("Resumen anual", fontWeight = FontWeight.SemiBold)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "ArrowForward")
    }

    // Generamos la estructura
    ElevatedCard(Modifier.height(100.dp).width(200.dp).background(Color.White)) {
        Row(
            Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Generamos las barras de color
            Box(
                boxModifier.background(color = Color.Red).weight(0.2f)
            )
            Spacer(Modifier.width(5.dp))
            Box(
                boxModifier.background(color = Color.Yellow).weight(0.5f)
            )
            Spacer(Modifier.width(5.dp))
            Box(
                boxModifier.background(color = Color.Green).weight(0.9f)
            )
        }
        // Generamos la leyenda de colores
    }
}