package com.es.appmovil.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.utils.customTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    label: String,
    expandido: Boolean,
    opciones: List<String>,
    seleccion: String,
    onExapandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit
) {
    var filtro by remember { mutableStateOf("") }

    val opcionesFiltradas = opciones.filter {
        it.contains(filtro, ignoreCase = true)
    }

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
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { onExapandedChange(false) }
        ) {
//            OutlinedTextField(
//                value = filtro,
//                onValueChange = { filtro = it },
//                label = { Text("Filtrar") },
//                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//            )

            opcionesFiltradas.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onValueChange(opcion)
                        filtro = ""
                        onExapandedChange(false)
                    }
                )
            }
        }
    }
}
