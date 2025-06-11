package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun HeaderSection(
    navigator: Navigator,
    tittle: String,
    icon: ImageVector,
    isIcon: Boolean,
    onDownloadClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.pop() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Return")
        }
        Text(tittle, fontWeight = FontWeight.Black, fontSize = 25.sp)
        if (isIcon)
            IconButton(onClick = onDownloadClick) {
                Icon(imageVector = icon, contentDescription = "")
            }
        else
            IconButton(onClick = {}, enabled = false) {
                Icon(imageVector = icon, contentDescription = "", tint = Color.White)
            }
    }
}