package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.viewmodel.DataViewModel.dailyHours
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.getHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.viewmodel.ResumeViewmodel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.ConteoHoras
import com.es.appmovil.widgets.DayDialog
import com.es.appmovil.widgets.LegendButton
import com.es.appmovil.widgets.ResumenHorasAnual
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.ResumenSemana

class ResumeScreen : Screen {
    @Composable
    override fun Content() {
        val resumeViewmodel = ResumeViewmodel()
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow
        var canClick by remember { mutableStateOf(true) }
        val currentHours by currentHours.collectAsState()
        val calendarViewmodel = CalendarViewModel()
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val dayMenuViewModel = DayMenuViewModel()
        val dailyHours by dailyHours.collectAsState()
        val currentDay by resumeViewmodel.currentDay.collectAsState()

        MaterialTheme {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navigator)
                },
                floatingActionButton = { ActionButton { calendarViewmodel.changeDialog(true) } },
                floatingActionButtonPosition = FabPosition.Center, // o End
                isFloatingActionButtonDocked = false
            ) {
                getHours()
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Resumen", fontWeight = FontWeight.Black, fontSize = 25.sp)
                        LegendButton(resumeViewmodel)
                    }

                    Spacer(Modifier.size(10.dp))

                    ResumenSemana(resumeViewmodel)
                    Spacer(Modifier.size(20.dp))

                    Row {
                        Column(Modifier.weight(1f)) {
                            ConteoHoras(currentHours, dailyHours, currentDay)
                            Spacer(Modifier.size(20.dp))
                            Column(Modifier.clickable {
                                if (canClick) {
                                    canClick = false
                                    navigator.push(AnualScreen())
                                }
                            }
                            ) {
                                ResumenHorasAnual(resumeViewmodel)
                            }
                        }

                        Spacer(Modifier.size(16.dp))

                        Column(Modifier.weight(1f).clickable {
                            if (canClick) {
                                canClick = false
                                navigator.push(CalendarScreen())
                            }
                        }
                        ) {
                            Row {
                                Text("Resumen mensual", fontWeight = FontWeight.SemiBold)
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "ArrowForward"
                                )
                            }
                            Spacer(Modifier.size(20.dp))
                            ResumenHorasMensual()
                        }
                    }

                    Spacer(Modifier.size(40.dp))

                    if (employee.idRol == 2) {
                        Button(
                            onClick = { navigator.push(AdminScreen()) },
                            elevation = ButtonDefaults.elevation(5.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(60.dp)
                        ) {
                            Text("ADMINISTRAR")
                        }
                    }
                }
                DayDialog(showDialog, today.value, dayMenuViewModel, calendarViewmodel) {
                    calendarViewmodel.changeDialog(it)
                }
            }
        }
    }
}

