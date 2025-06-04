package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.AnualViewModel
import com.es.appmovil.viewmodel.DataViewModel.currentHours
import com.es.appmovil.viewmodel.DataViewModel.employee
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
            }) { innerPadding ->
                Column(Modifier.padding(innerPadding).fillMaxWidth()) {
                    Text("Resumen Anual", fontWeight = FontWeight.Black, fontSize = 25.sp, modifier = Modifier.padding(horizontal = 16.dp).padding(top =16.dp))

                    Spacer(Modifier.size(30.dp))
                    LazyColumn {
                        item {
                            Column(Modifier.padding(horizontal = 16.dp)) {
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
                                            },
                                            enabled = index == 2,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            if (index == 2) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = {
                                                if (index != 2) {
                                                    anualViewModel.changeIndex(2)
                                                }
                                            },
                                            enabled = index == 1,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            if (index == 1) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                    }

                                }

                                Spacer(Modifier.size(10.dp))

                                ResumenAnual(anualViewModel)

                                Spacer(Modifier.size(30.dp))

                                TeoricoAnual(anualViewModel)
                            }

                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 62.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val meses = listOf("E", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
                                meses.forEach { mes ->
                                    Text(
                                        text = mes,
                                        fontSize = 12.sp,
                                        modifier = if (mes != "D") Modifier.padding(end = 16.dp) else Modifier.padding(end = 12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(Modifier.size(16.dp))
                        }

                        item {
                            val yearData = anualViewModel.getEmployeeYearData(employee.idEmployee)

                            val cards = listOf(
                                "V. Disfrutadas" to (yearData?.enjoyedHolidays?.toString() ?: "-"),
                                "V. Restantes" to (yearData?.currentHolidays?.toString() ?: "-")
                            )

                            Text("Vacaciones")

                            cards.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowItems.forEach { (label, value) ->
                                        Card(
                                            modifier = Modifier.weight(1f),
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(12.dp)
                                                    .fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(text = label, fontSize = 14.sp, color = Color.Gray)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = value,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.size(30.dp))
                        }

                    }
                }
            }
        }
    }
}