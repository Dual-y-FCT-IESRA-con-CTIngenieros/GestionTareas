package com.es.appmovil

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.getMonth
import com.es.appmovil.viewmodel.UserViewModel
import com.es.appmovil.widgets.FullScreenLoader
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        val userViewmodel = UserViewModel()
        DataViewModel
        getMonth()
        FullScreenLoader()

        Navigator(screen = LoginScreen(userViewmodel))
//        { navigator: Navigator ->
//            SlideTransition(navigator)
//        }
    }
}