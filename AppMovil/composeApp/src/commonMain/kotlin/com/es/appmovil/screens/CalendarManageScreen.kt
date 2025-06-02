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
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.TableView
import androidx.compose.runtime.Composable
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
import com.es.appmovil.viewmodel.CalendarManageViewModel
import com.es.appmovil.viewmodel.CalendarBlockWeekViewModel
import com.es.appmovil.viewmodel.CalendarYearViewModel
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.widgets.BottomNavigationBar

class CalendarManageScreen(private val calendarManageViewModel: CalendarManageViewModel) : Screen {
    @Composable
    override fun Content() {
        val size = 90
        var canClick by remember { mutableStateOf(true) }
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow

        val calendarBlockWeekViewModel = CalendarBlockWeekViewModel()
        val calendarYearViewModel = CalendarYearViewModel()

        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) {
                resetToday()
                calendarBlockWeekViewModel.resetWeeks()

                Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Gestión De Calendario",
                            fontWeight = FontWeight.Black,
                            fontSize = 25.sp
                        )
                    }

                    Spacer(Modifier.size(30.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ElevatedCard(
                            colors = CardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(CalendarBlockWeekScreen(calendarBlockWeekViewModel))
                                    canClick = false
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                androidx.compose.material.Icon(
                                    imageVector = Icons.Filled.SupervisedUserCircle,
                                    contentDescription = "Semanal",
                                    modifier = Modifier.size(size.dp),
                                    tint = Color(0xFF707272)
                                )
                                androidx.compose.material3.Text("Gestión Semanal")
                            }

                        }

                        Spacer(Modifier.size(16.dp))

                        ElevatedCard(
                            colors = CardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    canClick = false
                                    navigator.push(CalendarYearScreen(calendarYearViewModel))
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                androidx.compose.material.Icon(
                                    imageVector = Icons.Filled.TableView,
                                    contentDescription = "Anual",
                                    modifier = Modifier.size(size.dp),
                                    tint = Color(0xFF707272)
                                )
                                Text("Gestión Anual")
                            }

                        }
                    }
                }
            }
        }
    }
}