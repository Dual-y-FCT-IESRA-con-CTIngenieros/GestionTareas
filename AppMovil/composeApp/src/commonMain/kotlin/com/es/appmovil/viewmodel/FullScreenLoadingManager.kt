package com.es.appmovil.viewmodel

import androidx.compose.runtime.mutableStateOf

/**
 * Gestor global de carga en pantalla completa.
 *
 * Permite mostrar u ocultar un indicador de carga de forma centralizada
 * desde cualquier ViewModel o componente de la app.
 */
object FullScreenLoadingManager {

    // Estado observable que indica si se debe mostrar la pantalla de carga
    val isLoading = mutableStateOf(false)

    /**
     * Activa la visualización del loader.
     */
    fun showLoader() {
        isLoading.value = true
    }

    /**
     * Desactiva la visualización del loader.
     */
    fun hideLoader() {
        isLoading.value = false
    }
}
