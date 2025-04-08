package com.es.appmovil.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ConteoHoras(){
    Column(Modifier.width(180.dp)) {
        Text("Horas realizadas", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.size(20.dp))
        ProtectionMeter(
            inputValue = 50,
            progressColors = listOf(Color.Red, Color.Green, Color.Green),
            innerGradient = Color.Black
        )
    }
}

@Composable
fun ProtectionMeter(
    modifier: Modifier = Modifier,
    inputValue: Int,
    progressColors: List<Color>,
    innerGradient: Color,
    percentageColor: Color = Color.Black
) {

    val meterValue = getMeterValue(inputValue)
    ElevatedCard(
        colors = CardColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.Black),
        modifier = modifier.width(180.dp).height(283.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Card(
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Black),
            modifier = modifier.size(180.dp),
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
                val sweepAngle = 240f
                val height = size.height
                val width = size.width
                val startAngle = 150f // Angulo donde empieza
                val arcHeight = height - 20.dp.toPx()
                val centerOffset = Offset(width / 2f, height / 2.09f) // Centro del gráfico

                // Calculo del angulo de la aguja utilizando el inputValue introducido
                val needleAngle = (meterValue / 100f) * sweepAngle + startAngle
                val needleLength = 160f // Este valor ajusta la longitud de la aguja
                val needleBaseWidth = 10f // Este valor ajusta el grosor de la base de la aguja
                // Ruta de la aguja
                val needlePath = getPath(centerOffset, needleAngle, needleLength, needleBaseWidth)

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
                    Brush.radialGradient(
                        listOf(
                            innerGradient.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ), width / 2f
                )

                drawCircle(Color.White, 24f, centerOffset)

                drawPath(
                    color = Color.Black,
                    path = needlePath
                )
            }
        }
        // Textos debajo de la barra de progreso
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$inputValue", fontSize = 20.sp, lineHeight = 28.sp, color = percentageColor)
            Text(text = "Horas realizadas", fontSize = 16.sp, lineHeight = 24.sp, color = Color.Black)
        }

    }
}

private fun getPath(centerOffset: Offset, needleAngle:Float, needleLength:Float, needleBaseWidth:Float):Path {
    return Path().apply {
        val angleRad = needleAngle.toRadians()
        val baseLeftRad = (needleAngle - 90).toRadians()
        val baseRightRad = (needleAngle + 90).toRadians()

        val topX = centerOffset.x + needleLength * cos(angleRad)
        val topY = centerOffset.y + needleLength * sin(angleRad)

        val baseLeftX = centerOffset.x + needleBaseWidth * cos(baseLeftRad)
        val baseLeftY = centerOffset.y + needleBaseWidth * sin(baseLeftRad)
        val baseRightX = centerOffset.x + needleBaseWidth * cos(baseRightRad)
        val baseRightY = centerOffset.y + needleBaseWidth * sin(baseRightRad)

        moveTo(topX, topY)
        lineTo(baseLeftX, baseLeftY)
        lineTo(baseRightX, baseRightY)
        close()
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

fun Float.toRadians(): Float = this * (PI / 180).toFloat()
