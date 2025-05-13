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


@Composable
fun ResumenAnual(anualViewModel: AnualViewModel) {
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
