package com.es.appmovil

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Navigator(screen = LoginScreen(userViewmodel))
        }
    }
}

expect suspend fun saveToDownloads(data: String, filename: String): Boolean
