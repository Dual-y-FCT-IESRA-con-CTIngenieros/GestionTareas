package com.es.appmovil.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.es.appmovil.viewmodel.CalendarViewModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount

@Composable
fun ResumenHorasDia(calendarViewModel: CalendarViewModel) {

    val actividades by calendarViewModel.employeeActivity.collectAsState()
    val activity = actividades.find { it.date == calendarViewModel.today.value.toString() }
    val hours = activity?.time?.toDouble() ?: 0.0
    val color = activity?.let { colorPorTimeCode(it.idTimeCode) } ?: Color.LightGray

    ColumnChart(
        modifier = Modifier.padding(16.dp).height(300.dp),
        maxValue = 24.0,
        minValue = 0.0,
        indicatorProperties = HorizontalIndicatorProperties(
            count = IndicatorCount.CountBased(7),
        ),
        data = remember {
            listOf(
                Bars(
                    label = "Jan",
                    values = listOf(
                        Bars.Data(label = "Linux", value = 8.0, color = SolidColor(Color.Red)),
                        Bars.Data(
                            label = activity?.idTimeCode.toString() ?: "",
                            value = hours,
                            color = SolidColor(color)
                        )
                    ),
                )
            )
        },
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 3.dp,
            thickness = 20.dp
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
    )
}