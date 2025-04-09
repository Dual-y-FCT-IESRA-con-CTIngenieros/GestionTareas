package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResumenHorasAnual() {
    // Creamos la variable de modificador base de los box
    val boxModifier = Modifier.height(20.dp).clip(shape = RoundedCornerShape(5.dp))

    val timeCodes = mapOf(
        Color.Green to 90,
        Color.Yellow to 30,
        Color.Red to 30
    )

    Row {
        Text("Resumen anual", fontWeight = FontWeight.SemiBold)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "ArrowForward")
    }

    Spacer(Modifier.size(20.dp))

    // Generamos la estructura
    ElevatedCard(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black),
        modifier = Modifier.size(180.dp, 63.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Spacer(Modifier.size(10.dp))
        Row(
            Modifier.padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val totalHours = timeCodes.values.sum().toFloat()

            timeCodes.forEach { (timeCode, hour) ->
                Box(
                    boxModifier.background(color = timeCode).weight(hour / totalHours)
                )
                Spacer(Modifier.width(5.dp))
            }

        }
        Spacer(Modifier.size(5.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
            timeCodes.forEach { (timeCode, hour) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(timeCode, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$hour", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
        Spacer(Modifier.size(10.dp))
    }
}