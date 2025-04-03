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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun Calendar() {
    var fechaActual by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }

    val diasDelMes = obtenerDiasDelMes(fechaActual.year, fechaActual.monthNumber)
    val primerDiaSemana = obtenerPrimerDiaSemana(fechaActual.year, fechaActual.monthNumber)

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
            IconButton(onClick = { fechaActual = fechaActual.minus(DatePeriod(months = 1)) }) {
                Text("<", fontSize = 24.sp)
            }
            Text("${fechaActual.month.name} ${fechaActual.year}", fontSize = 20.sp)
            IconButton(onClick = { fechaActual = fechaActual.plus(DatePeriod(months = 1)) }) {
                Text(">", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val diasSemana = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            diasSemana.forEach { dia ->
                Text(text = dia, fontSize = 16.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(primerDiaSemana) { Spacer(modifier = Modifier.size(40.dp)) }

            // Días del mes
            items(diasDelMes) { dia ->
                val day = dia + 1

                if (day == fechaActual.dayOfMonth){
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Cyan)
                            .clickable {  showDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = day.toString(), fontSize = 16.sp)
                    }
                }else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable { showDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = day.toString(), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DayDialog(showDialog: Boolean, onChangeDialog:(Boolean)-> Unit) {

    if (showDialog) {
        Dialog(onDismissRequest = { onChangeDialog(false) }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Text("Dia")
                    TextField(value = "", onValueChange = {})
                    Text("Horas")
                    TextField(value = "", onValueChange = {})
                    Text("Codigo")
                    TextField(value = "", onValueChange = {})

                    Button({onChangeDialog(false)}) {
                        Text("Guardar")
                    }
                }
            }
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
fun obtenerPrimerDiaSemana(anio: Int, mes: Int): Int {
    val primerDia = LocalDate(anio, mes, 1).dayOfWeek
    return primerDia.ordinal  // Ajuste para empezar en lunes
}

fun diaActual(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}