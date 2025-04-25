package com.es.appmovil.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.TimeCode
import com.es.appmovil.viewmodel.AnualViewModel
import ir.ehsannarmani.compose_charts.RowChart
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ResumenAnual(anualViewModel: AnualViewModel) {
    val data = remember {
        listOf(
            Bars(
                label = "Jan",
                values = listOf(
                    Bars.Data(label = "Linux", value = 50.0, color = SolidColor(Color.Blue)),
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
                    Bars.Data(label = "Linux", value = 80.0, color = SolidColor(Color.Blue)),
                    Bars.Data(label = "Windows", value = 60.0, color = SolidColor(Color.Red))
                ),
            ),
        )
    }
    val data2 by anualViewModel.bars.collectAsState()
    RowChart(
        modifier = Modifier.height(325.dp),
        data = data2,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 1.dp,
            thickness = 8.dp
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false)
    )
}



