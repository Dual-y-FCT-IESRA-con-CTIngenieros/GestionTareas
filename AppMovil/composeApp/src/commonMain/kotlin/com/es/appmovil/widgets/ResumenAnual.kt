package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties

@Composable
fun ResumenAnual() {
    val data = remember {
        listOf(
            Bars(
                label = "Jan",
                values = listOf(
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 70.0, color = SolidColor(Color.Red))
                ),
            ),
            Bars(
                label = "Feb",
                values = listOf(
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Red))
                ),
            ),
            Bars(
                label = "Mar",
                values = listOf(
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 70.0, color = SolidColor(Color.Red))
                ),
            ),
            Bars(
                label = "Apr",
                values = listOf(
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Red))
                ),
            ),
            Bars(
                label = "May",
                values = listOf(
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 70.0, color = SolidColor(Color.Red))
                ),
            ),
            Bars(
                label = "Jun",
                values = listOf(
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Red))
                ),
            ),
        )
    }

    Row(Modifier.fillMaxWidth()) {
        ColumnChart(
            modifier = Modifier.height(300.dp),
            data = data,
            barProperties = BarProperties(
                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                spacing = 3.dp,
                thickness = 8.dp
            ),
            labelHelperProperties = LabelHelperProperties(enabled = false)
        )
    }
}


