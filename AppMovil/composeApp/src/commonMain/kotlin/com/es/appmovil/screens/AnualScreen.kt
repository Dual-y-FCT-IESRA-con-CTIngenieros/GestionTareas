package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.AnualViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.ResumenAnual
import com.es.appmovil.widgets.TeoricoAnual

class AnualScreen : Screen {
    @Composable
    override fun Content() {
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow
        val anualViewModel = AnualViewModel()
        val index by anualViewModel.index.collectAsState()
        val currentHours by currentHours.collectAsState()


        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) {
                Column(Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp)) {

                    Text("Resumen Anual", fontWeight = FontWeight.Black, fontSize = 25.sp)

                    Spacer(Modifier.size(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Horas Totales: $currentHours")
                        Row {
                            IconButton(
                                onClick = {
                                    if (index != 1) {
                                        anualViewModel.changeIndex(1)
                                    }
                                }, modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                                    contentDescription = ""
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (index != 2) {
                                        anualViewModel.changeIndex(2)
                                    }
                                }, modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                    contentDescription = ""
                                )
                            }
                        }

                    }

                    Spacer(Modifier.size(10.dp))

                    ResumenAnual(anualViewModel)

                    Spacer(Modifier.size(20.dp))

                    Text("Teórico")

                    Spacer(Modifier.size(10.dp))

                    TeoricoAnual(anualViewModel)

                }
            }
        }
    }
}