package com.es.appmovil.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.es.appmovil.utils.customTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(expandido: Boolean, opciones: List<String>, seleccion: String, onExapandedChange: (Boolean) -> Unit, onValueChange: (String) -> Unit){

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
            label = { Text("Rol") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { onExapandedChange(false) }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onValueChange(opcion)
                        onExapandedChange(false)
                    }
                )
            }
        }
    }
}