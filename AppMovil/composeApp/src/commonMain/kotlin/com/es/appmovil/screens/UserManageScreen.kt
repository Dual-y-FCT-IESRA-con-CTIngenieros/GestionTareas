package com.es.appmovil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.widgets.UserData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.es.appmovil.viewmodel.DataViewModel


class UserManageScreen: Screen {
    @Composable
    override fun Content() {

        LaunchedEffect(Unit){
            DataViewModel.cargarEmployees()
            DataViewModel.cargarRoles()
        }

        val employees = DataViewModel.employees.value
        val roles = DataViewModel.roles.value

        var showDialog by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxSize().padding(top = 30.dp, start = 16.dp, end = 16.dp)){
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Usuarios",
                    fontWeight = FontWeight.Black,
                    fontSize = 25.sp
                )
            }
            LazyColumn {
                employees.forEachIndexed { index, employee ->
                    item{ UserData(showDialog, index, employee, roles) { showDialog = it } }
                }
            }
        }
    }
}