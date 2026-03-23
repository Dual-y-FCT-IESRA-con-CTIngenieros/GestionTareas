package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.database.Database.supabase
import com.es.appmovil.screens.AnualScreen
import com.es.appmovil.screens.CalendarScreen
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.screens.ResumeScreen
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.UserViewModel
import com.russhwolf.settings.Settings
import io.github.jan.supabase.auth.auth

/**
 * Composable que renderiza la barra de navegación inferior con navegación entre pantallas.
 *
 * @param navigator Controlador de navegación de Voyager.
 */
@Composable
fun BottomNavigationBar(navigator: Navigator) {
    // Calcular el índice seleccionado en cada recomposición
    val selected = when (navigator.lastItem) {
        is ResumeScreen -> 0
        is CalendarScreen -> 1
        is AnualScreen -> 2
        else -> -1
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
            .graphicsLayer { shadowElevation = 10f },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Calendar icon (izquierda)
        IconButton(onClick = {
            if (selected != 1) {
                resetToday()
                navigator.replaceAll(CalendarScreen())
            }
        }) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Calendar",
                tint = if (selected == 1) Color(0xFFF4A900) else Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
        // Home icon (centro, como botón normal, solo el icono naranja)
        IconButton(
            onClick = {
                if (selected != 0) {
                    resetToday()
                    navigator.replaceAll(ResumeScreen())
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home",
                tint = if (selected == 0) Color(0xFFF4A900) else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
        }
        // Anual icon (derecha)
        IconButton(onClick = {
            if (selected != 2) {
                navigator.replaceAll(AnualScreen())
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Anual",
                tint = if (selected == 2) Color(0xFFF4A900) else Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


/**
 * Realiza el cierre de sesión del usuario:
 * - Cierra sesión en Supabase.
 * - Elimina los tokens almacenados.
 * - Redirige a la pantalla de login.
 *
 * @param navigator Controlador de navegación para redirigir.
 */
suspend fun signOut(navigator: Navigator) {
    val settings = Settings()
    supabase.auth.signOut()
    settings.remove("access_token")
    settings.remove("refresh_token")
    navigator.replaceAll(LoginScreen(UserViewModel()))
}
