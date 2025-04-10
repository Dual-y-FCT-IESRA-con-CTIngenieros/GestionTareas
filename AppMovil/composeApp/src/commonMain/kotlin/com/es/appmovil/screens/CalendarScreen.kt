package com.es.appmovil.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
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

        val calendarViewmodel = CalendarViewModel()

        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) { innerPadding ->

                Box(Modifier.padding(innerPadding).fillMaxSize()){
                    Column {
                        Calendar(calendarViewmodel)
                        Row{
                            ResumenHorasDia(
                                modifier = Modifier.weight(1f).height(400.dp).padding(20.dp)
                            )
                            Spacer(Modifier.size(1.dp))
                            ResumenHorasMensual()
                        }
                        ActionButton { }
                    }
                }
            }
        }
    }
}