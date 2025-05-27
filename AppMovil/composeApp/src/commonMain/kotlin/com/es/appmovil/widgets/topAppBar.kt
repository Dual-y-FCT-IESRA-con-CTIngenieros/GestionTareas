package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.screens.AnualScreen
import com.es.appmovil.screens.CalendarScreen
import com.es.appmovil.screens.ResumeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar(navigator: Navigator) {
    val currentScreen = navigator.lastItem
    val title = when (currentScreen) {
        is ResumeScreen -> "Resumen"
        is CalendarScreen -> "Calendario"
        is AnualScreen -> "Anual"
        else -> ""
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                if (currentScreen is ResumeScreen) {
                    IconButton(onClick = { /* Acción del ícono */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Opciones"
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}