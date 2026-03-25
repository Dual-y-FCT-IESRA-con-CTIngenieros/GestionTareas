package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.database.Database.supabase
import com.es.appmovil.screens.CalendarScreen
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.screens.HomeScreen
import com.es.appmovil.screens.ResumenScreen
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.UserViewModel
import com.russhwolf.settings.Settings
import io.github.jan.supabase.auth.auth

/**
 * Composable que renderiza la barra de navegación inferior con navegación entre pantallas.
 * Diseño adaptado al mockup: fondo blanco con borde redondeado superior y icono seleccionado resaltado.
 */
@Composable
fun BottomNavigationBar(navigator: Navigator) {
    val selected = when (navigator.lastItem) {
        is ResumenScreen -> 0
        is HomeScreen -> 1
        is CalendarScreen -> 2
        else -> -1
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .graphicsLayer { shadowElevation = 8f },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Resumen icon (izquierda)
        IconButton(
            onClick = {
                if (selected != 0) {
                    resetToday()
                    navigator.replaceAll(ResumenScreen())
                }
            }
        ) {
            if (selected == 0) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .graphicsLayer { shadowElevation = 8f },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Resumen",
                        tint = Color(0xFFF4A900),
                        modifier = Modifier.size(26.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Resumen",
                    tint = Color.Gray,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Home icon (centro)
        IconButton(onClick = {
            if (selected != 1) {
                resetToday()
                navigator.replaceAll(HomeScreen())
            }
        }) {
            if (selected == 1) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White, shape = RoundedCornerShape(24.dp))
                        .graphicsLayer { shadowElevation = 10f },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = Color(0xFFF4A900),
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Calendar icon (derecha)
        IconButton(onClick = {
            if (selected != 2) {
                resetToday()
                navigator.replaceAll(CalendarScreen())
            }
        }) {
            if (selected == 2) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .graphicsLayer { shadowElevation = 8f },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Calendar",
                        tint = Color(0xFFF4A900),
                        modifier = Modifier.size(26.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Calendar",
                    tint = Color.Gray,
                    modifier = Modifier.size(26.dp)
                )
            }
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
