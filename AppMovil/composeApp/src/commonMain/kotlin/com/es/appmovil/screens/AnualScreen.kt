package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.model.UserYearData
import com.es.appmovil.viewmodel.AnualViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.ResumenAnual
import com.es.appmovil.widgets.TeoricoAnual

/**
 * Pantalla de Resumen Anual.
 * Muestra:
 * - Horas totales trabajadas
 * - Resumen y datos teóricos anuales
 * - Vacaciones disfrutadas y restantes
 */
class AnualScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val anualViewModel = AnualViewModel()
        val index by anualViewModel.index.collectAsState()
        val currentHours by currentHours.collectAsState()

        MaterialTheme {
            Scaffold(
                bottomBar = { BottomNavigationBar(navigator) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding).fillMaxWidth()
                ) {
                    ScreenTitle("Resumen Anual")

                    Spacer(Modifier.size(30.dp))

                    LazyColumn {
                        item {
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                HeaderHoursNavigation(
                                    currentHours = currentHours,
                                    index = index,
                                    onChangeIndex = { anualViewModel.changeIndex(it) }
                                )

                                Spacer(Modifier.size(10.dp))

                                ResumenAnual(anualViewModel)

                                Spacer(Modifier.size(30.dp))

                                TeoricoAnual(anualViewModel)
                            }
                        }

                        item { MonthsRow() }

                        item {
                            val yearData = anualViewModel.getEmployeeYearData(employee.idEmployee)
                            VacationsSummary(yearData)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente para mostrar un título grande y destacado en la pantalla.
 * @param title Texto a mostrar como título
 */
@Composable
private fun ScreenTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Black,
        fontSize = 25.sp,
        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)
    )
}

/**
 * Barra superior que muestra las horas totales y botones para navegar entre diferentes vistas del resumen anual.
 * @param currentHours Horas totales trabajadas a mostrar
 * @param index Índice de la vista actual (por ejemplo 1 o 2)
 * @param onChangeIndex Callback para cambiar la vista seleccionada
 */
@Composable
private fun HeaderHoursNavigation(
    currentHours: Int,
    index: Int,
    onChangeIndex: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Horas Totales: $currentHours")

        Row {
            IconButton(
                onClick = { if (index != 1) onChangeIndex(1) },
                enabled = index == 2,
                modifier = Modifier.size(24.dp)
            ) {
                if (index == 2) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                        contentDescription = "Ir a resumen anterior"
                    )
                }
            }
            IconButton(
                onClick = { if (index != 2) onChangeIndex(2) },
                enabled = index == 1,
                modifier = Modifier.size(24.dp)
            ) {
                if (index == 1) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "Ir a resumen siguiente"
                    )
                }
            }
        }
    }
}

/**
 * Fila horizontal que muestra las abreviaturas de los meses del año.
 */
@Composable
private fun MonthsRow() {
    val meses = listOf("E", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 62.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        meses.forEachIndexed { index, mes ->
            Text(
                text = mes,
                fontSize = 12.sp,
                modifier = if (index != meses.lastIndex) Modifier.padding(end = 16.dp) else Modifier.padding(
                    end = 12.dp
                ),
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(Modifier.size(16.dp))
}

/**
 * Componente que muestra el resumen de vacaciones disfrutadas y restantes en tarjetas.
 * @param yearData Datos del año del empleado con información de vacaciones
 */
@Composable
private fun VacationsSummary(yearData: UserYearData?) {
    val cards = listOf(
        "V. Disfrutadas" to (yearData?.enjoyedHolidays?.toString() ?: "-"),
        "V. Restantes" to (yearData?.currentHolidays?.toString() ?: "-")
    )

    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Vacaciones")
        }

        Column(Modifier.padding(horizontal = 16.dp)) {
            cards.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { (label, value) ->
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) { VacationCard(label = label, value = value) }
                    }
                }
            }
        }

        Spacer(Modifier.size(30.dp))
    }
}

/**
 * Tarjeta individual que muestra un dato de vacaciones con su etiqueta y valor.
 * @param label Texto descriptivo del dato (ejemplo: "V. Disfrutadas")
 * @param value Valor numérico o texto a mostrar
 */
@Composable
private fun VacationCard(label: String, value: String) {

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}