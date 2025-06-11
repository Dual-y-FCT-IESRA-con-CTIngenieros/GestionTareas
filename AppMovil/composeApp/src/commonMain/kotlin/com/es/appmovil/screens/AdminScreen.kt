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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material3.Text
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
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.EmployeesDataViewModel
import com.es.appmovil.widgets.BottomNavigationBar

/**
 * Pantalla principal de administración.
 * Permite navegar a:
 * - Gestión de Usuarios
 * - Gestión de Tablas
 * - Gestión de Calendario
 */
class AdminScreen : Screen {
    @Composable
    override fun Content() {
        val size = 90 // Tamaño base para los íconos
        var canClick by remember { mutableStateOf(true) } // Previene múltiples clics rápidos
        val navigator = LocalNavigator.currentOrThrow // Objeto de navegación actual
        val employeesDataViewModel = EmployeesDataViewModel() // ViewModel para empleados

        MaterialTheme {
            Scaffold(
                bottomBar = { BottomNavigationBar(navigator) } // Barra de navegación inferior
            ) {
                resetToday() // Resetea el estado del día actual si es necesario

                Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)) {
                    // Título principal
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Administración",
                            fontWeight = FontWeight.Black,
                            fontSize = 25.sp
                        )
                    }

                    Spacer(Modifier.size(30.dp)) // Espaciado vertical

                    // Fila con las dos primeras opciones: Usuarios y Tablas
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Opción: Gestión de Usuarios
                        ElevatedCard(
                            colors = CardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f).height(180.dp).clickable {
                                if (canClick) {
                                    navigator.push(UserManageScreen(employeesDataViewModel = employeesDataViewModel))
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
                                Icon(
                                    imageVector = Icons.Filled.SupervisedUserCircle,
                                    contentDescription = "Usuario",
                                    modifier = Modifier.size(size.dp),
                                    tint = Color(0xFF707272)
                                )
                                Text("Gestión de Usuarios")
                            }
                        }

                        Spacer(Modifier.size(16.dp)) // Espaciado entre tarjetas

                        // Opción: Gestión de Tablas
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
                                    navigator.push(TableManageScreen())
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.TableView,
                                    contentDescription = "Tabla",
                                    modifier = Modifier.size(size.dp),
                                    tint = Color(0xFF707272)
                                )
                                Text("Gestión de Tablas")
                            }
                        }
                    }

                    Spacer(Modifier.size(16.dp)) // Espacio entre filas

                    // Fila con una sola tarjeta: Gestión de Calendario
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
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
                                    canClick = false
                                    navigator.push(CalendarManageScreen())
                                }
                            },
                            elevation = CardDefaults.elevatedCardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.EditCalendar,
                                    contentDescription = "Calendario",
                                    modifier = Modifier.size(size.dp),
                                    tint = Color(0xFF707272)
                                )
                                Text("Gestión de calendario")
                            }
                        }
                    }
                }
            }
        }
    }
}