package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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

    var monthChangeFlag = true
    // Creamos las variables necesarias desde el viewmodel
    val fechaActual by calendarViewmodel.today.collectAsState()
    val actividades by calendarViewmodel.employeeActivity.collectAsState()

    // Creamos la variable que nos permite mostrar el dialogo
    var showDialog by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(fechaActual) }
    calendarViewmodel.generarBarrasPorDia(date)

    /**
     * Obtenemos el número de días del mes, el mes anterior y el siguiente
     * mediate unas fucniones
     */
    val diasDelMes = obtenerDiasDelMes(fechaActual.year, fechaActual.monthNumber)
    val mesAnterior = primerDiaMes(fechaActual.year, fechaActual.monthNumber)
    val mesSiguiente = obtenerMesSiguiente(fechaActual.year, fechaActual.monthNumber)

    // Lista de dias de la semana abreviados
    val diasSemana = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")


    // Modificador del mes siguiente o anterior
    var otherMonthModifier = Modifier
        .size(40.dp)
        .clip(RoundedCornerShape(50))
        .padding(4.dp)
        .background(Color.LightGray.copy(alpha = 0.3f))
        .graphicsLayer { alpha = 0.3f }


    DayDialog(showDialog, date, calendarViewmodel) {
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
            IconButton(onClick = {
                if (monthChangeFlag){
                    monthChangeFlag = false
                    calendarViewmodel.onMonthChangePrevious(DatePeriod(months = 1))
                }
            }) {
                Text("<", fontSize = 24.sp)
            }
            Text(
                "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
                fontSize = 20.sp,
                modifier = Modifier.clickable { calendarViewmodel.resetMonth() })
            IconButton(onClick = {
                if (monthChangeFlag){
                    monthChangeFlag = false
                    calendarViewmodel.onMonthChangeFordward(DatePeriod(months = 1))
                }
            }) {
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
                val dayPrevMonth = mesAnterior - dia
                val currentDate = if (fechaActual.monthNumber == 1) {
                    LocalDate(fechaActual.year.minus(1), 12, dayPrevMonth)
                } else {
                    LocalDate(fechaActual.year, fechaActual.monthNumber.minus(1), dayPrevMonth)
                }


                // Buscar si hay una actividad en esa fecha
                val actividad = actividades.find { it.date == currentDate.toString() }

                val color = actividad?.let { colorPorTimeCode(it.idTimeCode) } ?: Color.LightGray

                val ultimoDiaMesAnterior =
                    LocalDate(fechaActual.year, fechaActual.monthNumber, 1)

                val ultimoDia = ultimoDiaMesAnterior
                    .minus(dayPrevMonth, DateTimeUnit.DAY).dayOfMonth

                otherMonthModifier = otherMonthModifier.clickable {
                    showDialog = true
                    date = LocalDate(fechaActual.year, fechaActual.monthNumber.minus(1), ultimoDia)
                    calendarViewmodel.generarBarrasPorDia(date)
                }
                Box(
                    modifier = otherMonthModifier.background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = ultimoDia.toString(), fontSize = 16.sp)
                }
            }

            // Días del mes
            items(diasDelMes) { dia ->
                val dayActualMonth = dia + 1
                val currentDate =
                    LocalDate(fechaActual.year, fechaActual.monthNumber, dayActualMonth)

                // Buscar si hay una actividad en esa fecha
                val actividad = actividades.find { it.date == currentDate.toString() }

                // Color por defecto o según actividad
                val color = actividad?.let { colorPorTimeCode(it.idTimeCode) } ?: Color.LightGray

                val modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .padding(4.dp)
                    .background(color)
                    .clickable {
                        showDialog = true
                        date = currentDate
                        calendarViewmodel.generarBarrasPorDia(date)
                    }

                val boxModifier = if (dayActualMonth == fechaActual.dayOfMonth) {
                    modifier.border(3.dp, Color(0xFFF5B014))
                } else modifier

                Box(
                    modifier = boxModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dayActualMonth.toString(), fontSize = 16.sp)
                }
            }

            // Dias del siguiente mes
            items(mesSiguiente) { dia ->
                val dayNextMonth = dia + 1
                val currentDate = if (fechaActual.monthNumber == 12) {
                    LocalDate(fechaActual.year.plus(1), 1, dayNextMonth)
                } else {
                    LocalDate(fechaActual.year, fechaActual.monthNumber.plus(1), dayNextMonth)
                }


                // Buscar si hay una actividad en esa fecha
                val actividad = actividades.find { it.date == currentDate.toString() }

                // Color por defecto o según actividad
                val color = actividad?.let { colorPorTimeCode(it.idTimeCode) } ?: Color.LightGray

                otherMonthModifier = otherMonthModifier.clickable {
                    showDialog = true
                    date =
                        LocalDate(fechaActual.year, fechaActual.monthNumber.plus(1), dayNextMonth)
                    calendarViewmodel.generarBarrasPorDia(date)
                }.background(color)

                Box(
                    modifier = otherMonthModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dayNextMonth.toString(), fontSize = 16.sp)
                }
            }
        }
    }
}

/**
 * Función para obtener el nombre del mes en español
 *
 * @param monthNumber Número del mes
 */
fun monthNameInSpanish(monthNumber: String): String {
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

fun colorPorTimeCode(code: Int): Color {
    return when (code) {
        100 -> Color.Green
        200 -> Color.Red
        555 -> Color.Blue
        900 -> Color.Yellow
        901 -> Color.Magenta
        else -> Color.LightGray
    }
}


/**
 * Función para obtener el número de días en un mes dado
 *
 * @param anio Número del año
 * @param mes Número del mes
 */
fun obtenerDiasDelMes(anio: Int, mes: Int): Int {
    val fecha = LocalDate(anio, mes, 1)

    return when (fecha.month) {
        kotlinx.datetime.Month.FEBRUARY -> if (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)) 29 else 28
        kotlinx.datetime.Month.APRIL, kotlinx.datetime.Month.JUNE,
        kotlinx.datetime.Month.SEPTEMBER, kotlinx.datetime.Month.NOVEMBER -> 30

        else -> 31
    }
}

/**
 *  Función para obtener el número del primer dia de la primera semana del mes dado
 *
 *  @param anio Número del año
 *  @param mes Número del mes
 */
fun primerDiaMes(anio: Int, mes: Int): Int {
    val primerDia = LocalDate(anio, mes, 1).dayOfWeek
    return primerDia.ordinal
}

/**
 * Función para obtener el número de los primeros dias del siguiente mes al dado
 *
 * @param anio Número del año
 * @param mes Número del mes
 */
fun obtenerMesSiguiente(anio: Int, mes: Int): Int {
    val primerDiaMesSiguiente = LocalDate(anio, mes, 1).plus(1, DateTimeUnit.MONTH)
    val ultimoDia = primerDiaMesSiguiente
        .minus(1, DateTimeUnit.DAY)

    val diaSemanaUltimoDia = ultimoDia.dayOfWeek.isoDayNumber // 1 (Lunes) - 7 (Domingo)
    return 7 - diaSemanaUltimoDia
}