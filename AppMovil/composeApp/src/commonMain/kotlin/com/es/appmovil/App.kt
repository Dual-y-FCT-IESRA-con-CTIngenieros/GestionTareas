package com.es.appmovil

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.screens.HomeScreen
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.viewmodel.DataViewModel
import com.es.appmovil.viewmodel.DataViewModel.getMonth
import com.es.appmovil.viewmodel.UserViewModel
import com.es.appmovil.widgets.FullScreenLoader
import com.es.appmovil.viewmodel.FullScreenLoadingManager
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    // Flag de desarrollo: si es true, iniciamos directamente en HomeScreen para ver cambios UI rápidamente.
    val startOnHome = true

    // Estado local de tema (UI-only). No modifica datos persistidos.
    var isDark by remember { mutableStateOf(false) }

    // Aquí usamos Material3 para mantener coherencia con otros widgets del proyecto
    MaterialTheme {
        val userViewmodel = UserViewModel()
        DataViewModel
        getMonth()
        // Aseguramos que el loader global no quede activado por accidente.
        FullScreenLoadingManager.hideLoader()
        FullScreenLoader()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Navigator(screen = if (startOnHome) HomeScreen() else LoginScreen(userViewmodel))
        }
    }
}

expect suspend fun saveToDownloads(data: String, filename: String): Boolean
