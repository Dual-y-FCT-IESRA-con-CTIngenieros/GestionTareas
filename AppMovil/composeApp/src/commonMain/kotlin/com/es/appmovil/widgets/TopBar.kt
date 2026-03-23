package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.es.appmovil.widgets.signOut
import kotlinx.coroutines.IO

@Composable
fun TopBar(
    navigator: Navigator,
    title: String = "",
    showLogout: Boolean = true,
    rightContent: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLogout) {
            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    signOut(navigator)
                }
            }) {
                Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", tint = Color(0xFFF4A900))
            }
        }
        Text(title, fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color.Black)
        if (rightContent != null) {
            rightContent()
        } else {
            IconButton(onClick = {}, enabled = false) {
                Icon(Icons.Filled.Logout, contentDescription = null, tint = Color.Transparent)
            }
        }
    }
}
