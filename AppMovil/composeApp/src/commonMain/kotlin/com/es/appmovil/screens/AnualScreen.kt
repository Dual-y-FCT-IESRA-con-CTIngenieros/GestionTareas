package com.es.appmovil.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.widgets.ResumenAnual
import com.es.appmovil.widgets.TeoricoAnual

class AnualScreen : Screen {
    @Composable
    override fun Content() {
        Column(Modifier.padding(top = 30.dp, start = 16.dp, end = 16.dp)) {

            Text("Resumen Anual", fontWeight = FontWeight.Black, fontSize = 25.sp)

            Spacer(Modifier.size(30.dp))

            Row {
                Text("Horas Totales:")
                Text("5")
            }

            ResumenAnual()

            Spacer(Modifier.size(20.dp))

            TeoricoAnual()

        }
    }
}