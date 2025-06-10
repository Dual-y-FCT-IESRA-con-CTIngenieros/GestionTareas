package com.es.appmovil.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.es.appmovil.database.Database.supabase
import com.es.appmovil.screens.AnualScreen
import com.es.appmovil.screens.CalendarScreen
import com.es.appmovil.screens.LoginScreen
import com.es.appmovil.screens.ResumeScreen
import com.es.appmovil.utils.customButtonColors
import com.es.appmovil.viewmodel.DataViewModel.resetToday
import com.es.appmovil.viewmodel.UserViewModel
import com.russhwolf.settings.Settings
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(navigator: Navigator) {
    val selected by remember { selectScreen(navigator) }
    val items = listOf("Home", "Calendar", "Anual", "Profile")
    var canClick by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.DateRange,
        Icons.Filled.Notifications,
        Icons.Filled.Person
    )
    val screenItems: List<Screen> = listOf(ResumeScreen(), CalendarScreen(), AnualScreen())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
            .graphicsLayer { shadowElevation = 10f }
    ) {
        val currentScreen = navigator.lastItem
        if(currentScreen is ResumeScreen || currentScreen is CalendarScreen) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val width = size.width
                val height = size.height
                val curveSize = 100f
                val curveHeight = 140f

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo((width / 2) - curveSize, 0f)
                    quadraticBezierTo(width / 2, curveHeight, (width / 2) + curveSize, 0f)
                    lineTo(width, 0f)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(path, color = Color.White)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->

                IconButton(onClick = {
                    if (selected != index) {
                        if (canClick) {
                            if (index == 3) {
                                canClick = true
                                showDialog = true
                            } else {
                                canClick = false
                                if (screenItems[index] !is ResumeScreen) {
                                    if (screenItems[index] is CalendarScreen)
                                        resetToday()
                                    navigator.push(screenItems[index])
                                } else {
                                    resetToday()
                                    navigator.replaceAll(ResumeScreen())
                                }
                            }


                        }
                    }
                }) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        tint = if (selected == index) Color(0xFFF4A900) else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    DialogSession(showDialog,
        {
            CoroutineScope(Dispatchers.IO).launch {
                signOut(navigator)
            }
            showDialog = false
        }
    ) {showDialog = false}
}

@Composable
fun DialogSession(showDialog:Boolean, onAccept:()-> Unit, onDismiss:()->  Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss ,
            confirmButton = {
                Button(onClick = onAccept, colors = customButtonColors()) {
                    Text("Cerrar Sesión")
                }
            },
            title = { Text("Sesión") },
            text = { Text("¿Quieres cerrar sesión?") }
        )
    }
}

    suspend fun signOut(navigator: Navigator) {
        val settings = Settings()
        supabase.auth.signOut()
        settings.remove("access_token")
        settings.remove("refresh_token")
        navigator.replaceAll(LoginScreen(UserViewModel()))
    }

fun selectScreen(navigator: Navigator): MutableState<Int> {
    val currentScreen = navigator.lastItem
    return when (currentScreen) {
        is ResumeScreen -> mutableStateOf(0)
        is CalendarScreen -> mutableStateOf(1)
        is AnualScreen -> mutableStateOf(2)
        else -> mutableStateOf(-1)
    }
}