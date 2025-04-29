package com.es.appmovil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.es.appmovil.database.Database.supabase
import com.es.appmovil.model.TimeCode
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.UserViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import ctingenierosappmovil.composeapp.generated.resources.Res
import ctingenierosappmovil.composeapp.generated.resources.compose_multiplatform
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@Composable
@Preview
fun App() {
    MaterialTheme {
        val userViewmodel = UserViewModel()
        DataViewModel.getHours()
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }

        Navigator(screen = LoginScreen(userViewmodel)) { navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}