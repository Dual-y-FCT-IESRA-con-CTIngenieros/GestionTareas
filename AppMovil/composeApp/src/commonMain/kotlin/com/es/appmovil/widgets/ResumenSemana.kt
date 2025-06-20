package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.es.appmovil.viewmodel.DataViewModel.currentToday
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.ResumeViewmodel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate

/**
 * Composable que muestra el resumen semanal.
 *
 * Permite visualizar los días de la semana actual con sus respectivas actividades y navegar entre semanas.
 *
 * @param resumeViewmodel ViewModel encargado de gestionar los datos de la semana.
 */
@Composable
fun ResumenSemana(resumeViewmodel: ResumeViewmodel) {

    val listState = rememberLazyListState()
    val dayModifier = Modifier.padding(end = 5.dp, start = 5.dp).background(Color.White)
        .width(65.dp).height(60.dp)

    val daysActivity = resumeViewmodel.getDayActivity()
    val fechaActual by today.collectAsState()
    var monthChangeFlag = true

    val semana = resumeViewmodel.getWeekDaysWithNeighbors(
        fechaActual.year,
        fechaActual.monthNumber,
        fechaActual.dayOfMonth
    )

    val todayIndex = semana.indexOf(fechaActual)
    val visibleItems = 4
    val targetIndex = (todayIndex - visibleItems / 2).coerceIn(0, semana.size - visibleItems)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (monthChangeFlag) {
                monthChangeFlag = false
                resumeViewmodel.onMonthChangePrevious(DatePeriod(days = 7))
            }
        }) {
            Text("<", fontSize = 20.sp)
        }
        Text(
            "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
            fontSize = 20.sp,
            modifier = Modifier.clickable { resetToday() })
        IconButton(onClick = {
            if (monthChangeFlag) {
                monthChangeFlag = false
                resumeViewmodel.onWeekChangeFordward(DatePeriod(days = 7))
            }
        }) {
            Text(">", fontSize = 20.sp)
        }
    }
    ElevatedCard(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth().height(70.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)

    ) {
        LaunchedEffect(Unit) {
            listState.animateScrollToItem(targetIndex)
        }

        LazyRow(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            state = listState
        ) {
            items(semana.size) {
                val diasSemana = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                Days(semana[it], dayModifier, diasSemana[it], daysActivity.value)
            }
        }
    }
}

/**
 * Composable que representa visualmente un único día dentro del resumen semanal.
 *
 * Aplica colores de fondo según las actividades del día.
 *
 * @param day Día que se va a mostrar.
 * @param dayModifier Modificador base para el diseño del día.
 * @param diaSemana Nombre del día de la semana (por ejemplo: "Lun", "Mar").
 * @param daysActivity Mapa de actividades del día, asociadas a sus colores.
 */
@Composable
fun Days(
    day: LocalDate,
    dayModifier: Modifier,
    diaSemana: String,
    daysActivity: MutableMap<String, MutableList<Color>>
) {
    val todayModifier = if (day == currentToday.value) {
        dayModifier.border(width = 2.dp, color = Color(0xFF000000))
    } else {
        dayModifier
    }

    var colors = mutableListOf<Color>()

    daysActivity.forEach { (fc, color) ->
        if (day.toString() == fc) colors = color
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



