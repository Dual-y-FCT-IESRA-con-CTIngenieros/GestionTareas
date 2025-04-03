package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.Calendar

class CalendarScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            Scaffold(bottomBar = {
                BottomNavigationBar(navigator)
            }) { innerPadding ->
                Column(Modifier.padding(innerPadding)) {
                    Calendar()
                }
            }
        }
    }
}