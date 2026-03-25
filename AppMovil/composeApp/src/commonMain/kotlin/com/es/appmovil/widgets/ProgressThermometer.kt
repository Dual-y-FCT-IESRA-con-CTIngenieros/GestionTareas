package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/**
 * Widget UI-only: termómetro de progreso con estilo cercano al mockup de Figma.
 * Muestra título, valor grande, subtítulo, barra de progreso con gradiente y botón registrar.
 */
@Composable
fun ProgressThermometer(
    currentHours: Float,
    targetHours: Float,
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {}
) {
    val safeTarget = if (targetHours <= 0f) 1f else targetHours
    val progress = (currentHours / safeTarget).coerceIn(0f, 1f)

    fun fmt(v: Float): String {
        return if (v % 1f == 0f) String.format(Locale.getDefault(), "%.0f", v) else String.format(Locale.getDefault(), "%.1f", v)
    }

    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = "Progreso Anual", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        // Valor grande centrado
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = "${fmt(currentHours)}/${fmt(targetHours)}", style = MaterialTheme.typography.headlineLarge.copy(color = Color(0xFFF36B1E)), textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Horas trabajadas este año", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de progreso: usar Box con fillMaxWidth(progress) para evitar usar weight(0)
        val gradient = Brush.horizontalGradient(listOf(Color(0xFFF84E42), Color(0xFF8BA66B)))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFFF0F0F0))
        ) {
            // Porción llena (puede ser 0f sin lanzar excepción)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(28.dp))
                    .background(gradient),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (progress > 0f) {
                    // knob: círculo con borde blanco y sombra (solo si hay porción llena)
                    Box(modifier = Modifier
                        .size(34.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .wrapContentSize(Alignment.Center)
                        .padding(2.dp)
                    ) {
                        Box(modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF36B1E)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0%", color = Color.Gray, fontSize = 12.sp)
            Text("50%", color = Color.Gray, fontSize = 12.sp)
            Text("100%", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Botón con gradiente y texto blanco (clickable aplicado correctamente)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFFF5B014), Color(0xFFF4A900))))
                .clickable { onRegisterClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Registrar Jornada", fontSize = 18.sp, color = Color.White)
        }
    }
}
