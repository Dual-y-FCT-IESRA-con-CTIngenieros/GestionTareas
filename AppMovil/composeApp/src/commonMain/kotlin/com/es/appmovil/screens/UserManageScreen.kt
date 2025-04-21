package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.es.appmovil.widgets.UserData


class UserManageScreen: Screen {

    @Composable
    override fun Content() {

        Column(Modifier.fillMaxSize()){
            Text("Usuarios")
            LazyColumn {
                UserData()
            }
        }
    }
}