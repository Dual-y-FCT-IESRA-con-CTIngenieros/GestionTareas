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

    fun checkLogin():Boolean {
        // Comprueba que los datos no estén vacíos
        return if (username.value.isNotBlank() && password.value.isNotBlank()) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    // Intenta iniciar sesión en la base de datos
                    Database.supabase.auth.signInWith(Email){
                        email = _username.value
                        password = _password.value
                    }
                }
                _password.value = ""
                true
            } catch (e:Exception) { // Si da error no ha podido iniciar sesión
                false
            }
        } else false

        //return username.value.isNotBlank() && password.value.isNotBlank()
    }
}