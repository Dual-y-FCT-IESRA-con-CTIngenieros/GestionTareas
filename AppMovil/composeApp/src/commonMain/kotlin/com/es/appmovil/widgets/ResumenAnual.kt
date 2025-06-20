package com.es.appmovil.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.viewmodel.AnualViewModel
import ir.ehsannarmani.compose_charts.RowChart
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties

/**
 * Composable que muestra el resumen anual en forma de gráfico de barras horizontales.
 *
 * Los datos se obtienen desde el AnualViewModel y se renderizan con RowChart.
 *
 * @param anualViewModel ViewModel que proporciona la lista de barras anuales.
 */
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
        labelHelperProperties = LabelHelperProperties(enabled = false),
        maxValue = 160.0
    )
}
