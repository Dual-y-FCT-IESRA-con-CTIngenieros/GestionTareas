package com.es.appmovil.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Composable que muestra un resumen de horas realizadas con un medidor visual.
 *
 * @param currentHours Horas actuales realizadas.
 * @param dailyHours Horas diarias esperadas.
 * @param currentDay Día actual del conteo.
 */
@Composable
fun ConteoHoras(currentHours: Int, dailyHours: Int, currentDay: Int) {
    Column(Modifier.width(180.dp)) {
        Text("Horas realizadas", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.size(20.dp))
        ProtectionMeter(
            currentHours = currentHours,
            progressColors = listOf(Color.Red, Color.Green, Color.Green),
            dailyHours = dailyHours,
            currentDay = currentDay
        )
    }
}


/**
 * Composable que dibuja un medidor circular semi-arco para mostrar progreso de horas.
 *
 * @param modifier Modificador para aplicar a la vista.
 * @param currentHours Horas actuales realizadas.
 * @param progressColors Lista de colores para el gradiente del arco.
 * @param dailyHours Horas diarias esperadas.
 * @param currentDay Día actual del conteo.
 */
@Composable
fun ProtectionMeter(
    modifier: Modifier = Modifier,
    currentHours: Int,
    progressColors: List<Color>,
    dailyHours: Int,
    currentDay: Int
) {
    val fechaActual by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }

    val meterValue = getMeterValue(currentHours)
    val maxValue = dailyHours * currentDay

    ElevatedCard(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        ),
        modifier = modifier.size(180.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Box(modifier = modifier.size(180.dp)) {

            ShowCanvas(meterValue, maxValue, progressColors)

            HorasRealizadas(modifier, meterValue, maxValue, fechaActual)
        }
    }
}

/**
 * Dibuja el arco de progreso y la aguja del medidor en un Canvas.
 *
 * @param meterValue Valor actual del medidor.
 * @param maxValue Valor máximo del medidor.
 * @param progressColors Colores para el gradiente del arco.
 */
@Composable
fun ShowCanvas(meterValue: Int, maxValue: Int, progressColors: List<Color>) {
    Canvas(modifier = Modifier.fillMaxSize().padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
        val sweepAngle = 180f
        val height = size.height
        val width = size.width
        val startAngle = 180f // Angulo donde empieza
        val arcHeight = height - 20.dp.toPx()
        val centerOffset = Offset(width / 1.96f, height / 2f) // Centro del gráfico

        // Falta mejorar el calculo de días

        //val totalDays = 1
        //val totalHours = dailyHours * currentDay
        val needleAngle = (meterValue / (maxValue.toFloat() * 2)) * sweepAngle + startAngle
        val needleLength = 183f // Este valor ajusta la posición del circulo

        val angleRad = needleAngle.toRadians()
        val circleX = centerOffset.x + needleLength * cos(angleRad)
        val circleY = centerOffset.y + needleLength * sin(angleRad)

        drawArc(
            brush = Brush.horizontalGradient(progressColors),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2),
            size = Size(arcHeight, arcHeight),
            style = Stroke(width = 50f, cap = StrokeCap.Round)
        )

        drawCircle(
            color = Color.Black,
            radius = 33f,
            center = Offset(circleX, circleY),
        )

        drawCircle(
            color = Color.White,
            radius = 20f,
            center = Offset(circleX, circleY),
        )

        drawCircle(
            color = if (meterValue >= maxValue) Color.Green else Color.Red,
            radius = 24f,
            center = Offset(circleX, circleY),
            style = Stroke(width = 16f)
        )
    }
}


/**
 * Muestra debajo del medidor el texto con horas realizadas y la fecha actual.
 *
 * @param modifier Modificador para aplicar.
 * @param meterValue Valor actual del medidor.
 * @param maxValue Valor máximo del medidor.
 * @param fechaActual Fecha actual para mostrar.
 */
@Composable
fun HorasRealizadas(modifier: Modifier, meterValue: Int, maxValue: Int, fechaActual: LocalDate) {
    Box(
        modifier = modifier.fillMaxWidth().height(163.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Textos debajo de la barra de progreso
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = if (meterValue >= maxValue) Color.Green else Color.Red)) {
                    append(meterValue.toString())
                }
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("/$maxValue")
                }
            }, fontSize = calculateFontSize(meterValue, maxValue), lineHeight = 28.sp)
            Text(
                text = "Horas realizadas",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Black
            )
            Text(text = formatearFecha(fechaActual), fontSize = 14.sp)
        }
    }
}


/**
 * Calcula el tamaño de fuente para el texto del medidor basado en la longitud del número.
 *
 * @param meterValue Valor actual del medidor.
 * @param maxValue Valor máximo del medidor.
 * @return Tamaño de fuente a usar.
 */
fun calculateFontSize(meterValue: Int, maxValue: Int): TextUnit {
    val longestLength = maxOf(meterValue.toString().length, maxValue.toString().length)
    return when (longestLength) {
        in 1..1 -> 42.sp
        in 2..2 -> 38.sp
        in 3..3 -> 30.sp
        in 4..4 -> 24.sp
        else -> 26.sp
    }
}

/**
 * Formatea una fecha en formato dd-MM-yyyy.
 *
 * @param fecha Fecha a formatear.
 * @return Cadena con la fecha formateada.
 */
fun formatearFecha(fecha: LocalDate): String {
    val dia = fecha.dayOfMonth.toString().padStart(2, '0')
    val mes = fecha.monthNumber.toString().padStart(2, '0')
    val anio = fecha.year.toString()
    return "$dia-$mes-$anio"
}

/**
 * Asegura que el valor del medidor no sea menor que cero.
 *
 * @param inputPercentage Valor de entrada.
 * @return Valor corregido, mínimo 0.
 */
private fun getMeterValue(inputPercentage: Int): Int {
    return if (inputPercentage < 0) {
        0
    } else {
        inputPercentage
    }
}

/**
 * Convierte un ángulo en grados a radianes.
 *
 * @receiver Ángulo en grados.
 * @return Ángulo en radianes.
 */
fun Float.toRadians(): Float = this * (PI / 180).toFloat()
