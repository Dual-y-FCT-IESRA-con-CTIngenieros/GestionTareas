package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.area

class UserWeekScreen:Screen {
    @Composable
    override fun Content() {
        val area by DataViewModel.area.collectAsState()
        var index by remember { mutableStateOf(0) }
        Column(Modifier.fillMaxSize()){
            Text("Semanas")
            LazyRow(Modifier.fillMaxWidth()) {
                items(area.size) {
                    Button({index = it}) {
                        Text(area[it].desc)
                    }
                }
            }

            val employeeByArea = getEmployeeByArea()

            LazyColumn {
                items(employeeByArea.size) {

                    Row {
                        Text(employeeByArea[it].key)
                        Text(employeeByArea[it].value)
                    }

                }
            }



        }
    }
}