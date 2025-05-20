package com.es.appmovil.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.es.appmovil.viewmodel.FullScreenLoadingManager

@Composable
fun FullScreenLoader(
    bgColor: Color = Color.White
) {
    val isLoading by FullScreenLoadingManager.isLoading

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 8.dp,
                    color = Color(0xFFF4A900)
                )
            }
        }
    }
}