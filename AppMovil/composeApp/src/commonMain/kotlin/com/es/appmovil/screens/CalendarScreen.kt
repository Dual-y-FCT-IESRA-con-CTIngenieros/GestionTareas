package com.es.appmovil.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.Calendar
import com.es.appmovil.widgets.ResumenHorasDia
import com.es.appmovil.widgets.ResumenHorasMensual

class CalendarScreen : Screen {
    @Composable
    override fun Content() {
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow
        val calendarViewmodel = CalendarViewModel()
        val dayMenuViewModel = DayMenuViewModel()

        // Creamos las variables necesarias desde el viewmodel
        val fechaActual by today.collectAsState()
        val actividades by calendarViewmodel.employeeActivity.collectAsState()
        val timeCodes by calendarViewmodel.timeCodes.collectAsState()

        // Creamos la variable que nos permite mostrar el dialogo
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val showDialogConfig by calendarViewmodel.showDialogConfig.collectAsState()

        DataViewModel.getPie()

        MaterialTheme {
            Scaffold(
                bottomBar = { BottomNavigationBar(navigator) },
                floatingActionButton = { ActionButton { calendarViewmodel.changeDialog(true) } },
                floatingActionButtonPosition = FabPosition.Center, // o End
                isFloatingActionButtonDocked = false
            ) { innerPadding ->

                Box(Modifier.padding(innerPadding).fillMaxSize()) {
                    Column {
                        Calendar(
                            calendarViewmodel,
                            dayMenuViewModel,
                            fechaActual,
                            showDialog,
                            showDialogConfig,
                            actividades,
                            timeCodes
                        )
                        Row {
                            ElevatedCard(
                                colors = CardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.Black
                                ),
                                modifier = Modifier.weight(1f)
                                    .padding(start = 20.dp, bottom = 20.dp).clickable {
                                    calendarViewmodel.changeDialogConfig(true)
                                },
                                elevation = CardDefaults.elevatedCardElevation(5.dp)

                            ) {
                                ResumenHorasDia(calendarViewmodel)
                            }

                            Spacer(Modifier.size(16.dp))

                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp, bottom = 10.dp)
                            ) {
                                ResumenHorasMensual() // ya no tiene medidas fijas dentro
                            }
                        }
                    }
                }
            }
        }
    }
}