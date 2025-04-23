package com.es.appmovil.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.ResumeViewmodel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.Calendar
import com.es.appmovil.widgets.ResumenHorasDia
import com.es.appmovil.widgets.ResumenHorasMensual

class CalendarScreen() : Screen {
    @Composable
    override fun Content() {
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow

        val resumeViewmodel = ResumeViewmodel()
        val calendarViewmodel = CalendarViewModel()

        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) { innerPadding ->

                Box(Modifier.padding(innerPadding).fillMaxSize()) {
                    Column {
                        Calendar(calendarViewmodel)
                        Row {
                            ElevatedCard(
                                colors = CardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.Black
                                ),
                                modifier = Modifier.weight(1f).padding(20.dp),
                                elevation = CardDefaults.elevatedCardElevation(5.dp)

                            ) {
                                ResumenHorasDia(calendarViewmodel)
                            }
                            Spacer(Modifier.size(1.dp))
                            Column {
                                Row {
                                    Text("Resumen del mes", fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(Modifier.size(20.dp))
                                ResumenHorasMensual(resumeViewmodel)
                            }
                        }
                        ActionButton { }
                    }
                }
            }
        }
    }
}