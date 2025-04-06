package com.es.appmovil.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.screens.CalendarScreen
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun ResumenHorasMensual() {
    var navigator = LocalNavigator.currentOrThrow
    var data by remember {
        mutableStateOf(
            listOf(
                Pie(
                    label = "Android",
                    data = 20.0,
                    color = Color.Red,
                    selectedColor = Color.Green
                ),
                Pie(
                    label = "Windows",
                    data = 45.0,
                    color = Color.Cyan,
                    selectedColor = Color.Blue
                ),
                Pie(
                    label = "Linux",
                    data = 35.0,
                    color = Color.Gray,
                    selectedColor = Color.Yellow,
                ),
            )
        )
    }
    Column(Modifier.clickable { navigator.push(CalendarScreen()) }) {
        Row{
            Text("Resumen mensual", fontWeight = FontWeight.SemiBold)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "ArrowForward")
        }

        Spacer(Modifier.size(20.dp))

        ElevatedCard(
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Black
            ),
            modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally),
            elevation = CardDefaults.elevatedCardElevation(5.dp)

        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.size(20.dp))

                PieChart(
                    modifier = Modifier.size(150.dp),
                    data = data,
                    onPieClick = {
                        val pieIndex = data.indexOf(it)
                        data =
                            data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                    },
                    selectedScale = 1.2f,
                    scaleAnimEnterSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    colorAnimEnterSpec = tween(300),
                    colorAnimExitSpec = tween(300),
                    scaleAnimExitSpec = tween(300),
                    spaceDegreeAnimExitSpec = tween(300),
                    selectedPaddingDegree = 4f,
                    style = Pie.Style.Stroke(width = 30.dp)
                )

                Spacer(Modifier.size(20.dp))

                Column(Modifier.padding(bottom = 20.dp, start = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Circle, contentDescription = "", Modifier.size(16.dp), tint = Color.Cyan)
                        Text("Producción - 100", modifier = Modifier.padding(start = 5.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Circle, contentDescription = "", Modifier.size(16.dp), tint = Color.Red)
                        Text("Producción - 100", modifier = Modifier.padding(start = 5.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Circle, contentDescription = "", Modifier.size(16.dp), tint = Color.Gray)
                        Text("Producción - 100", modifier = Modifier.padding(start = 5.dp))
                    }
                }
            }
        }
    }
}