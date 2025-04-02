package com.es.appmovil.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase viewmodel para el usuario, donde guardaremos los datos del usuario y sus posibles funciones
 */
class UserViewmodel {

    // Nombre de usuario del trabajador con el que iniciará sesión.
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    // Contraseña del usuario con la que iniciará sesión.
    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // Contraseña del usuario con la que iniciará sesión.
    private var _visibility = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    // Actualiza las variables para que se reflejen en la pantalla.
    fun onChangeValue(name:String, pass:String) {
        _username.value = name
       _password.value = pass
    }

    fun onChangeVisibility() {
        _visibility.value = !_visibility.value
    }

    fun checkLogin() = username.value.isNotBlank() && password.value.isNotBlank()

}