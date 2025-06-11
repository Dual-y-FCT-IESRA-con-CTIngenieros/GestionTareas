package com.es.appmovil.utils

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Proporciona colores personalizados para botones.
 *
 * @return Un objeto [ButtonColors] con los colores definidos:
 * - Fondo amarillo (#F4A900)
 * - Texto negro
 * - Fondo deshabilitado gris
 * - Texto deshabilitado negro
 */
@Composable
fun customButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        backgroundColor = Color(0xFFF4A900),
        contentColor = Color.Black,
        disabledBackgroundColor = Color.Gray,
        disabledContentColor = Color.Black
    )
}

/**
 * Proporciona colores personalizados para campos de texto outlined.
 *
 * @return Un objeto [TextFieldColors] con los colores definidos:
 * - Texto negro
 * - Fondo transparente
 * - Borde enfocado amarillo (#F4A900)
 * - Borde no enfocado gris
 * - Etiqueta enfocado amarillo (#F4A900)
 * - Etiqueta no enfocado gris
 * - Cursor amarillo (#F4A900)
 */
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