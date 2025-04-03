package com.es.appmovil.viewmodel

import com.es.appmovil.database.Database
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Clase viewmodel para el usuario, donde guardaremos los datos del usuario y sus posibles funciones
 */
class UserViewmodel {

    // Nombre de usuario del trabajador con el que iniciará sesión.
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    // Contraseña del usuario con la que iniciará sesión.
    private var _password = MutableStateFlow("")
    val passwordText: StateFlow<String> = _password

    // Contraseña del usuario con la que iniciará sesión.
    private var _visibility = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    // Contraseña del usuario con la que iniciará sesión.
    private var _login = MutableStateFlow(false)
    val login: StateFlow<Boolean> = _login

    // Actualiza las variables para que se reflejen en la pantalla.
    fun onChangeValue(name:String, pass:String) {
        _username.value = name
       _password.value = pass
    }

    fun onChangeVisibility() {
        _visibility.value = !_visibility.value
    }

    fun checkLogin() {
        // Comprueba que los datos no estén vacíos
        if (username.value.isNotBlank() && passwordText.value.isNotBlank()) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    // Intenta iniciar sesión en la base de datos
                    Database.supabase.auth.signInWith(Email){
                        email = _username.value
                        password = _password.value
                    }
                    _login.value = true
                }
            } catch (e:Exception) { // Si da error no ha podido iniciar sesión
                false
            }
        }

        //return username.value.isNotBlank() && password.value.isNotBlank()
    }

    fun resetVar() {
        _visibility.value = false
        _login.value = false
        _username.value = ""
        _password.value = ""
    }
}