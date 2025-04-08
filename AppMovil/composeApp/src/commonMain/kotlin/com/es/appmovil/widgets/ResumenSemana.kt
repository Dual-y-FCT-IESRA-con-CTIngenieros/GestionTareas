package com.es.appmovil.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun ResumenSemana() {

    val dayModifier = Modifier.padding(end = 5.dp, start = 5.dp).background(Color.White)
    .width(70.dp).height(70.dp)

    Text("Semana", fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.size(10.dp))
    ElevatedCard(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth().height(70.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)

    ){
        val fechaActual by remember {
            mutableStateOf(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
        }
        LazyRow(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            val semana = getWeekDaysWithNeighbors(
                fechaActual.year,
                fechaActual.monthNumber,
                fechaActual.dayOfMonth
            )

            items(semana.size) {
                Days(semana[it], fechaActual, dayModifier)
            }
        }
    }
    Spacer(Modifier.size(10.dp))
    Leyenda()
}

@Composable
fun Days(day:LocalDate, fechaActual:LocalDate, dayModifier: Modifier) {
    val todayModifier = if (day.dayOfMonth == fechaActual.dayOfMonth) {
        dayModifier.border(width = 2.dp, color = Color.Yellow)
    }else{
        dayModifier
    }

    Column(
        todayModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(day.dayOfMonth.toString())
        Text(day.month.toString())

    }

}

@Composable
fun Leyenda(){
    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
        Row(Modifier.padding(end = 10.dp),verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(Color.Green)
            }
            Text("Prod", modifier = Modifier.padding(start = 5.dp))
        }
        Row(Modifier.padding(end = 10.dp),verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(Color.Red)
            }
            Text("Ausencia", modifier = Modifier.padding(start = 5.dp))
        }
        Row(Modifier.padding(end = 10.dp),verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(Color.Yellow)
            }
            Text("En espera", modifier = Modifier.padding(start = 5.dp))
        }
        Row(Modifier.padding(end = 10.dp),verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(Color.Gray)
            }
            Text("Nada", modifier = Modifier.padding(start = 5.dp))
        }
    }
}

fun getWeekDaysWithNeighbors(year: Int, month: Int, day: Int): List<LocalDate> {
    val selectedDate = LocalDate(year, month, day)
    val dayOfWeek = selectedDate.dayOfWeek.isoDayNumber // 1 (Lunes) a 7 (Domingo)

    val firstDayOfWeek = selectedDate.minus(dayOfWeek - 1, DateTimeUnit.DAY)
    val lastDayOfWeek = firstDayOfWeek.plus(6, DateTimeUnit.DAY)

    return (0..6).map { firstDayOfWeek.plus(it, DateTimeUnit.DAY) }
}