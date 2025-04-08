package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.ConteoHoras
import com.es.appmovil.widgets.ResumenHorasAnual
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.ResumenSemana


class ResumeScreen: Screen{
    @Composable
    override fun Content(){
        // Generamos la navegación actual
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) {
                Column(Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
                    Text("Resumen", fontWeight = FontWeight.Black, fontSize = 25.sp)
                    Column (Modifier.padding(top = 30.dp)){
                        ResumenSemana()
                        Spacer(Modifier.size(50.dp))
                        Row {
                            Column(Modifier.weight(1f)){
                                ConteoHoras()
                                Spacer(Modifier.size(20.dp))
                                ResumenHorasAnual()
                            }
                            ResumenHorasMensual()
                        }
                    }
                }

            }
        }


    }
}