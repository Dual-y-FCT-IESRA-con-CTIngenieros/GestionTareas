package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class TableManageScreen : Screen {
    @Composable
    override fun Content() {
        Column(Modifier.fillMaxSize()) {
            Text("Gestión Tablas")
            val listaTablas = mutableListOf<String>()
            LazyColumn {
                items(listaTablas.size) {
                    TableData(listaTablas[it])
                }
            }
        }
    }

    @Composable
    fun TableData(tabla:String) {
        Row(Modifier.fillMaxWidth() ) {

        }
    }
}

