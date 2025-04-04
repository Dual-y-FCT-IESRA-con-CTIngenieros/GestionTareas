package com.es.appmovil.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ConteoHoras(){
    Column(Modifier.width(200.dp)) {
        Text("Horas realizadas", fontWeight = FontWeight.SemiBold)
        ProtectionMeter(
            inputValue = 100,
            progressColors = listOf(Color.Red, Color.Yellow, Color.Green),
            innerGradient = Color.Black
        )
    }
}

@Composable
fun ProtectionMeter(
    modifier: Modifier = Modifier,
    inputValue: Int,
    trackColor: Color = Color(0xFFE0E0E0),
    progressColors: List<Color>,
    innerGradient: Color,
    percentageColor: Color = Color.Black
) {

    val meterValue = getMeterValue(inputValue)
    Box(modifier = modifier.size(196.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = 240f
            val fillSwipeAngle = (meterValue / 100f) * sweepAngle
            val height = size.height
            val width = size.width
            val startAngle = 150f
            val arcHeight = height - 20.dp.toPx()

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2f),
                size = Size(arcHeight, arcHeight),
                style = Stroke(width = 50f, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.horizontalGradient(progressColors),
                startAngle = startAngle,
                sweepAngle = fillSwipeAngle,
                useCenter = false,
                topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2),
                size = Size(arcHeight, arcHeight),
                style = Stroke(width = 50f, cap = StrokeCap.Round)
            )
            val centerOffset = Offset(width / 2f, height / 2.09f)
            drawCircle(
                Brush.radialGradient(
                    listOf(
                        innerGradient.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                ), width / 2f
            )
            drawCircle(Color.White, 24f, centerOffset)

            // Calculo del angulo de la aguja utilizando el inputValue introducido
            val needleAngle = (meterValue / 100f) * sweepAngle + startAngle
            val needleLength = 160f // Este valor ajusta la longitud de la aguja
            val needleBaseWidth = 10f // Este valor ajusta el grosor de la base de la aguja

            // Ruta de la aguja
            val needlePath = Path().apply {
                // Calculo de la punta de la aguja
                val topX = centerOffset.x + needleLength * cos(
                    needleAngle.toDouble().toFloat()
                )
                val topY = centerOffset.y + needleLength * sin(
                    needleAngle.toDouble().toFloat()
                )

                // Calculo de la base de la aguja
                val baseLeftX = centerOffset.x + needleBaseWidth * cos(
                    (needleAngle - 90).toDouble().toFloat()
                )
                val baseLeftY = centerOffset.y + needleBaseWidth * sin(
                    (needleAngle - 90).toDouble().toFloat()
                )
                val baseRightX = centerOffset.x + needleBaseWidth * cos(
                    (needleAngle + 90).toDouble().toFloat()
                )
                val baseRightY = centerOffset.y + needleBaseWidth * sin(
                    (needleAngle + 90).toDouble().toFloat()
                )

                moveTo(topX, topY)
                lineTo(baseLeftX, baseLeftY)
                lineTo(baseRightX, baseRightY)
                close()
            }

            drawPath(
                color = Color.Black,
                path = needlePath
            )
        }
        // Textos debajo de la barra de progreso
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$inputValue", fontSize = 20.sp, lineHeight = 28.sp, color = percentageColor)
            Text(text = "Horas realizadas", fontSize = 16.sp, lineHeight = 24.sp, color = Color.Black)
        }

    }
}

private fun getMeterValue(inputPercentage: Int): Int {
    return if (inputPercentage < 0) {
        0
    } else if (inputPercentage > 100) {
        100
    } else {
        inputPercentage
    }
}

