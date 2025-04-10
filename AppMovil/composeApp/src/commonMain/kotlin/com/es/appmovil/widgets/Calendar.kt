package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.es.appmovil.viewmodel.CalendarViewModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus


@Composable
fun Calendar(calendarViewmodel: CalendarViewModel) {

    val fechaActual by calendarViewmodel.today.collectAsState()
    val year by calendarViewmodel.year.collectAsState()
    val month by calendarViewmodel.month.collectAsState()

    // Obtenemos el número de días del mes, el mes anterior y el siguiente
    // mediate unas fucniones
    val diasDelMes = obtenerDiasDelMes(year, fechaActual.monthNumber)
    val mesAnterior = obtenerMesPasado(year, fechaActual.monthNumber)
    val mesSiguiente = obtenerMesSiguiente(year, fechaActual.monthNumber)

    val diasSemana = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    var showDialog by remember { mutableStateOf(false) }

    DayDialog(showDialog) {
        showDialog = false
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { calendarViewmodel.onMonthChangePrevious(DatePeriod(months = 1))}) {
                Text("<", fontSize = 24.sp)
            }
            Text(
                "${mesEspanish(month.name)} $year",
                fontSize = 20.sp,
                modifier = Modifier.clickable { calendarViewmodel.resetMonth() })
            IconButton(onClick = { calendarViewmodel.onMonthChangeFordward(DatePeriod(months = 1)) }) {
                Text(">", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Generamos los dias de la semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            diasSemana.forEach { dia ->
                Text(text = dia, fontSize = 16.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Generamos el calendario
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {

            // Dias del mes anterior
            items(mesAnterior) { dia ->
                val day = mesAnterior - dia

                val ultimoDiaMesAnterior =
                    LocalDate(fechaActual.year, fechaActual.monthNumber, 1)

                val ultimoDia = ultimoDiaMesAnterior
                    .minus(day, DateTimeUnit.DAY).dayOfMonth

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.3f))
                        .clickable { showDialog = true }
                        .graphicsLayer { alpha = 0.3f },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = ultimoDia.toString(), fontSize = 16.sp)
                }
            }

            // Días del mes
            items(diasDelMes) { dia ->
                val dayPrevMonth = dia + 1

                // cambia el color del dia actual
                if (dayPrevMonth == fechaActual.dayOfMonth && fechaActual.monthNumber == fechaActual.monthNumber) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Cyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = dayPrevMonth.toString(), fontSize = 16.sp)
                    }
                } else {
                    // si no por defecto (gris)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = dayPrevMonth.toString(), fontSize = 16.sp)
                    }
                }
            }

            // Dias del siguiente mes
            items(mesSiguiente) { dia ->
                val day = dia + 1

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Magenta.copy(alpha = 0.3f))
                        .graphicsLayer { alpha = 0.3f },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day.toString(), fontSize = 16.sp)
                }
            }
        }
    }
}

fun mesEspanish(monthNumber: String): String {
    return when (monthNumber) {
        "JANUARY" -> "Enero"
        "FEBRUARY" -> "Febrero"
        "MARCH" -> "Marzo"
        "APRIL" -> "Abril"
        "MAY" -> "Mayo"
        "JUNE" -> "Junio"
        "JULY" -> "Julio"
        "AUGUST" -> "Agosto"
        "SEPTEMBER" -> "Septiembre"
        "OCTOBER" -> "Octubre"
        "NOVEMBER" -> "Noviembre"
        "DECEMBER" -> "Diciembre"
        else -> monthNumber
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDialog(showDialog: Boolean, onChangeDialog: (Boolean) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                onChangeDialog(showDialog)
            },
            sheetState = sheetState
        ) {
            // Contenido de la sheet
            Text("Contenido deslizable")
        }
    }
}

fun obtenerDiasDelMes(anio: Int, mes: Int): Int {
    val fecha = LocalDate(anio, mes, 1)

    return when (fecha.month) {
        kotlinx.datetime.Month.FEBRUARY -> if (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)) 29 else 28
        kotlinx.datetime.Month.APRIL, kotlinx.datetime.Month.JUNE,
        kotlinx.datetime.Month.SEPTEMBER, kotlinx.datetime.Month.NOVEMBER -> 30

        else -> 31
    }
}

// Función para obtener el día de la semana del primer día del mes (0 = Lunes, 6 = Domingo)
fun obtenerMesPasado(anio: Int, mes: Int): Int {
    val primerDia = LocalDate(anio, mes, 1).dayOfWeek
    return primerDia.ordinal  // Ajuste para empezar en lunes
}

fun obtenerMesSiguiente(anio: Int, mes: Int): Int {
    val primerDiaMesSiguiente = LocalDate(anio, mes, 1).plus(1, DateTimeUnit.MONTH)
    val ultimoDia = primerDiaMesSiguiente
        .minus(1, DateTimeUnit.DAY)

    val diaSemanaUltimoDia = ultimoDia.dayOfWeek.isoDayNumber // 1 (Lunes) - 7 (Domingo)
    return 7 - diaSemanaUltimoDia
}