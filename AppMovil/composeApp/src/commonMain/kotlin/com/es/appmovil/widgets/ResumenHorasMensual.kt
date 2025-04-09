package com.es.appmovil.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun ResumenHorasMensual() {
    val dataGraphic by remember { getGraphic() }
        Column {
            ElevatedCard(
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                ),
                modifier = Modifier.width(180.dp).align(Alignment.CenterHorizontally),
                elevation = CardDefaults.elevatedCardElevation(5.dp)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.size(20.dp))

                    Box(
                        modifier = Modifier
                            .size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PieChart(
                            modifier = Modifier.size(150.dp),
                            data = dataGraphic,
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
                            spaceDegree = 6f,
                            style = Pie.Style.Stroke(width = 30.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total")
                            Text("${dataGraphic.sumOf { it.data.toInt() }}", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.size(20.dp))

                    PieInfo(dataGraphic)
                }
            }
        }

}

@Composable
fun PieInfo(data: List<Pie>) {
    LazyColumn(Modifier.padding(bottom = 20.dp)) {
        items(data.size) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    Modifier
                        .size(12.dp)
                        .background(data[it].color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${data[it].data.toInt()}")
            }
        }
    }
}

fun getGraphic():MutableState<List<Pie>> {
    return mutableStateOf(
        listOf(
            Pie(
                label = "Aus",
                data = 20.0,
                color = Color.Red,
                selectedColor = Color.Red
            ),
            Pie(
                label = "Prd",
                data = 45.0,
                color = Color.Green,
                selectedColor = Color.Green
            ),
            Pie(
                label = "Vac",
                data = 35.0,
                color = Color.Yellow,
                selectedColor = Color.Yellow,
            ),
        )
    )
}
