package com.es.appmovil.utils

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun customButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        backgroundColor = Color(0xFFF4A900),
        contentColor = Color.Black,
        disabledBackgroundColor = Color.Gray,
        disabledContentColor = Color.Black
    )
}

@Composable
fun customTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        textColor = Color.Black,
        backgroundColor = Color.Transparent,
        focusedBorderColor = Color(0xFFF4A900),
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color(0xFFF4A900),
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color(0xFFF4A900)
    )
}