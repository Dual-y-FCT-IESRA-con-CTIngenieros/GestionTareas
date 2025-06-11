package com.es.appmovil.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.es.appmovil.utils.customTextFieldColors

/**
 * @description Componente que muestra un campo de texto para filtrar
 * @param showFilter Booleano que indica si se debe mostrar el campo de texto
 * @param filter String que contiene el valor del campo de texto
 * @param onFilterChange Función que se llama cuando se cambia el valor del campo de texto
 */

@Composable
fun genericFilter(showFilter: Boolean, filter: String, onFilterChange: (String) -> Unit) {
    if (showFilter) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            OutlinedTextField(
                value = filter,
                onValueChange = { onFilterChange(it) },
                label = { Text("Filtro") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = customTextFieldColors()
            )
        }
    }
}