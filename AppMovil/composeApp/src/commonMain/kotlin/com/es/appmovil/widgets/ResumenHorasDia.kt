package com.es.appmovil.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    val data by calendarViewModel.bars.collectAsState()

    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total de hoy:")
            Text("${data.sumOf { bar -> bar.values.sumOf { it.value } }}", fontWeight = FontWeight.Bold)
        }

        ColumnChart(
            modifier = Modifier.padding(16.dp).height(300.dp),
            maxValue = 14.0,
            minValue = 0.0,
            indicatorProperties = HorizontalIndicatorProperties(
                count = IndicatorCount.CountBased(8),
            ),
            data = data,
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


}