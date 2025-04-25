package com.es.appmovil.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.viewmodel.CalendarViewModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties

@Composable
fun ResumenHorasDia(calendarViewModel: CalendarViewModel) {

//  val actividades by calendarViewModel.employeeActivity.collectAsState()
//  val activity = actividades.find { it.date == calendarViewModel.today.value.toString() }
//  val hours = activity?.time?.toDouble() ?: 0.0
//  val color = activity?.let { colorPorTimeCode(it.idTimeCode) } ?: Color.LightGray
    val data by calendarViewModel.bars.collectAsState()

//    val activiti = EmployeeActivity(
//        idEmployee = 1,
//        idWorkOrder = 101,
//        idTimeCode = 3,
//        idActivity = 45,
//        time = 7.5f,
//        date = "2025-04-25",
//        comment = "Trabajo en campo"
//    )

//    val bars = listOf(
//        Bars.Data(
//            label = activiti.idTimeCode.toString(),
//            value = activiti.time.toDouble(),
//            color = SolidColor(Color.Green)
//        ),
//        Bars.Data(
//            label = "900",
//            value = 10.0,
//            color = SolidColor(Color.Yellow)
//        ),
//    )

    ColumnChart(
        modifier = Modifier.padding(16.dp).height(300.dp),
        maxValue = 12.0,
        minValue = 0.0,
        indicatorProperties = HorizontalIndicatorProperties(
            count = IndicatorCount.CountBased(7),
        ),
        data = data,
//        remember {
//            listOf(
//                Bars(
//                    label = activiti.date,
//                    values = bars,
//                )
//            )
//        },
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 3.dp,
            thickness = 20.dp
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false)
    )
}