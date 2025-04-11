package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.es.appmovil.viewmodel.ResumeViewmodel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun ResumenSemana(resumeViewmodel: ResumeViewmodel) {

    val listState = rememberLazyListState()
    val dayModifier = Modifier.padding(end = 5.dp, start = 5.dp).background(Color.White)
    .width(65.dp).height(60.dp)

    val daysActivity = resumeViewmodel.getDayActivity()

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

        val semana = getWeekDaysWithNeighbors(
            fechaActual.year,
            fechaActual.monthNumber,
            fechaActual.dayOfMonth
        )

        val todayIndex = semana.indexOf(fechaActual)
        val visibleItems = 4
        val targetIndex = (todayIndex - visibleItems / 2).coerceIn(0, semana.size - visibleItems)

        LaunchedEffect(Unit) {
            listState.animateScrollToItem(targetIndex)
        }

        LazyRow(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, state = listState) {
            items(semana.size) {
                val diasSemana = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                Days(semana[it], fechaActual, dayModifier, diasSemana[it], daysActivity.value)
            }
        }
    }
}

@Composable
fun Days(day:LocalDate, fechaActual:LocalDate, dayModifier: Modifier, diaSemana:String, daysActivity:MutableMap<String, MutableList<Color>>) {
    val todayModifier = if (day == fechaActual) {
        dayModifier.border(width = 2.dp, color = Color(0xFF000000))
    }else{
        dayModifier
    }

    var colors = mutableListOf<Color>()

    daysActivity.forEach { (fc, color) ->
        if(day.toString() == fc) colors = color
    }

    val backgroundModifier = if (colors.size >= 2) {
        todayModifier.background(Brush.linearGradient(colors))
    } else if (colors.isNotEmpty()) {
        todayModifier.background(colors[0]) // Usamos un solo color si solo hay uno
    } else {
        todayModifier
    }

    Column(
        modifier = backgroundModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(day.dayOfMonth.toString())
        Text(diaSemana)
    }

}

fun getWeekDaysWithNeighbors(year: Int, month: Int, day: Int): List<LocalDate> {
    val selectedDate = LocalDate(year, month, day)
    val dayOfWeek = selectedDate.dayOfWeek.isoDayNumber // 1 (Lunes) a 7 (Domingo)

    val firstDayOfWeek = selectedDate.minus(dayOfWeek - 1, DateTimeUnit.DAY)

    return (0..6).map { firstDayOfWeek.plus(it, DateTimeUnit.DAY) }
}

