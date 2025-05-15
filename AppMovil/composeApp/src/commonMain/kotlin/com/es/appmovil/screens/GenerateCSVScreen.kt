package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.utils.ManageCSV

class GenerateCSVScreen:Screen {
    @Composable
    override fun Content() {
        val manageCSV = ManageCSV()
        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Exportar a CSV",
                    fontWeight = FontWeight.Black,
                    fontSize = 25.sp
                )
            }

            Spacer(Modifier.size(40.dp))

            Button({manageCSV.generateCSV1()}, Modifier.fillMaxWidth()) {
                Text("Descargar CSV 1")
            }

            Button({manageCSV.generateCSV("")}, Modifier.fillMaxWidth()) {
                Text("Descargar CSV 2")
            }
        }
    }
}