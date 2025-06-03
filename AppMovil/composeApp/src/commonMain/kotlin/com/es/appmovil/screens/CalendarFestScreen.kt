package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.viewmodel.CalendarFestViewModel
import com.es.appmovil.viewmodel.DataViewModel.calendar
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.monthNameInSpanish
import kotlinx.datetime.DatePeriod

class CalendarFestScreen(private val calendarFestViewModel: CalendarFestViewModel):Screen {
    @Composable
    override fun Content() {
        var monthChangeFlag = true
        val fechaActual by today.collectAsState()

        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarFestViewModel.onMonthChangePrevious(DatePeriod(years = 1))
                    }
                }) {
                    Text("<", fontSize = 24.sp)
                }
                Text(
                    "${monthNameInSpanish(fechaActual.month.name)} ${fechaActual.year}",
                    fontSize = 20.sp,
                    modifier = Modifier.clickable { resetToday() })
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarFestViewModel.onMonthChangeFordward(DatePeriod(years = 1))
                    }
                }) {
                    Text(">", fontSize = 24.sp)
                }
            }


            LazyColumn {
                item {
                    val festivos = calendar.value.filter { it.idCalendar.toString() == fechaActual.year.toString() }
                    festivos.forEach { fest ->
                        ElevatedCard(
                            colors = CardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth().height(70.dp).padding(vertical = 8.dp),
                            elevation = CardDefaults.elevatedCardElevation(5.dp)
                        ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(fest.date)

                                IconButton({}) {
                                    Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = "")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}