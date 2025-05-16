package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.viewmodel.CalendarManageViewModel
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.widgets.monthNameInSpanish
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate

class CalendarManageScreen(private val calendarManageViewModel:CalendarManageViewModel) : Screen {
    @Composable
    override fun Content() {
        var monthChangeFlag = true
        val fechaActual by today.collectAsState()
        val showDialog by calendarManageViewModel.showDialog.collectAsState()
        val weekIndex by calendarManageViewModel.weekIndex.collectAsState()
        val locked by calendarManageViewModel.locked.collectAsState()
        val weeksInMonth by calendarManageViewModel.weeksInMonth.collectAsState()


        if (showDialog) {
            DialogLock(
                week = weeksInMonth,
                index =  weekIndex,
                locked = locked,
                onAdd = {
                    calendarManageViewModel.lockWeek(weeksInMonth[weekIndex])
                    calendarManageViewModel.changeDialog(false)},
                onRemove = {
                    calendarManageViewModel.unlockWeek(weeksInMonth[weekIndex])
                    calendarManageViewModel.changeDialog(false)
                }) { calendarManageViewModel.changeDialog(false) }
        }

        Column(Modifier.fillMaxSize().padding(top = 30.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (monthChangeFlag) {
                        monthChangeFlag = false
                        calendarManageViewModel.onMonthChangePrevious(DatePeriod(months = 1))
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
                        calendarManageViewModel.onMonthChangeFordward(DatePeriod(months = 1))
                    }
                }) {
                    Text(">", fontSize = 24.sp)
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(weeksInMonth.size) { index ->
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
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Semana del ${weeksInMonth[index].first.dayOfMonth} al ${weeksInMonth[index].second.dayOfMonth}")
                            IconButton(onClick = {
                                calendarManageViewModel.changeDialog(true)
                                calendarManageViewModel.changeWeekIndex(index)
                            }) {
                                if (weeksInMonth[index] in locked) Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                                else Icon(
                                    imageVector = Icons.Filled.LockOpen,
                                    contentDescription = "",
                                    tint = Color.Green
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DialogLock(
        week: List<Pair<LocalDate, LocalDate>>,
        index: Int,
        locked: List<Pair<LocalDate, LocalDate>>,
        onAdd: () -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = if (week[index] !in locked) "¿Quieres bloquear esta fecha?" else "¿Quieres desbloquear esta fecha?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = onDismiss) {
                            Text("Cancelar")
                        }
                        Button(onClick = if (week[index] !in locked) onAdd else onRemove) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}