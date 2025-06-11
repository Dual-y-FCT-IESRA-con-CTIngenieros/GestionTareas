package com.es.appmovil.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.es.appmovil.utils.customTextFieldColors

/**
 * Composable para un menú desplegable con opciones seleccionables.
 *
 * @param label Etiqueta del campo de texto.
 * @param expandido Estado que indica si el menú está abierto.
 * @param opciones Mapa de opciones donde la clave es el valor interno y el valor es el texto mostrado.
 * @param seleccion Opción actualmente seleccionada (texto).
 * @param onExapandedChange Callback para cambiar el estado de expansión del menú.
 * @param onValueChange Callback para notificar el cambio de selección con la opción seleccionada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    label: String,
    expandido: Boolean,
    opciones: Map<String, String>,
    seleccion: String,
    onExapandedChange: (Boolean) -> Unit,
    onValueChange: (Map<String, String>) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { onExapandedChange(!expandido) },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            colors = customTextFieldColors(),
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { onExapandedChange(false) }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.value) },
                    onClick = {
                        onValueChange(mapOf(opcion.key to opcion.value))
                        onExapandedChange(false)
                    }
                )
            }
        }
    }
}
