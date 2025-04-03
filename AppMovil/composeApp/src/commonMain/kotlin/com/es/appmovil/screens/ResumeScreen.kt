package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.widgets.ConteoHoras
import com.es.appmovil.widgets.ResumenHorasAnual
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.ResumenSemana


class ResumeScreen: Screen{
    @Composable
    override fun Content(){
        // Generamos la navegación actual
        val navigator = LocalNavigator.current

        Text("Resumen", fontWeight = FontWeight.Black, fontSize = 25.sp)
        Column(Modifier.fillMaxSize()) {
            ResumenSemana()
            Row {
                Column(Modifier.weight(1f)){
                    ConteoHoras()
                    ResumenHorasAnual()
                }
                ResumenHorasMensual()
            }
        }
    }
}