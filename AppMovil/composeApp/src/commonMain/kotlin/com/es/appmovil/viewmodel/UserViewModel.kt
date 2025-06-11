package com.es.appmovil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.es.appmovil.database.Database
import com.es.appmovil.database.Database.supabase
import com.russhwolf.settings.Settings
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel encargado de gestionar el login, sesión y datos de usuario.
 */
class UserViewModel : ViewModel() {

    // Estado para el nombre de usuario introducido
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    // Estado para el email (completo tras formatear)
    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    init {
        // Al inicializar intentamos cargar el email almacenado
        viewModelScope.launch {
            FullScreenLoadingManager.showLoader()
            _email.value = DataViewModel.cargarYObtenerEmail()
            FullScreenLoadingManager.hideLoader()
        }
    }

    // Estado para la contraseña
    private var _password = MutableStateFlow("")
    val passwordText: StateFlow<String> = _password

    // Indica si es necesario cambiar la contraseña
    private var _passChange = MutableStateFlow(false)
    val passChange: StateFlow<Boolean> = _passChange

    // Estado para mostrar u ocultar la contraseña
    private var _visibility = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    // Estado para indicar si el login fue correcto
    private var _login = MutableStateFlow(false)
    val login: StateFlow<Boolean> = _login

    // Estado para indicar si se ha comprobado la sesión
    private var _checkSess = MutableStateFlow(false)
    val checkSess: StateFlow<Boolean> = _checkSess

    // Mensaje de error del login
    private var _loginErrorMessage = MutableStateFlow("")
    val loginErrorMessage: StateFlow<String> = _loginErrorMessage

    // Estado que indica si hubo error de login
    private var _loginError = MutableStateFlow(false)
    val loginError: StateFlow<Boolean> = _loginError

    /**
     * Actualiza el usuario y contraseña introducidos por el usuario.
     */
    fun onChangeValue(name: String, pass: String) {
        _username.value = name
        _password.value = pass
    }

    /**
     * Alterna el estado de necesidad de cambio de contraseña.
     */
    fun onPassChangeChange() {
        _passChange.value = !_passChange.value
    }

    /**
     * Completa el email si solo se introdujo el usuario.
     * Ejemplo: "usuario" => "usuario@empresa.com"
     */
    private fun completeEmail() {
        val regex = ".+@".toRegex()
        if (!regex.containsMatchIn(_email.value)) {
            _email.value = _username.value + _email.value
        }
    }

    /**
     * Alterna la visibilidad de la contraseña.
     */
    fun onChangeVisibility() {
        _visibility.value = !_visibility.value
    }

    /**
     * Inicia sesión con Supabase comprobando los datos introducidos.
     * Gestiona tanto el login como el guardado de tokens locales.
     */
    fun checkLogin() {
        // Verifica que los campos no estén vacíos
        if (username.value.isNotBlank() && passwordText.value.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    completeEmail() // Completa el email si hace falta

                    // Llama al login de Supabase
                    supabase.auth.signInWith(Email) {
                        email = _email.value
                        password = _password.value
                    }

                    _login.value = true

                    // Recupera la sesión y guarda los tokens en Settings
                    val session = supabase.auth.currentSessionOrNull()
                    if (session != null) {
                        val settings = Settings()
                        settings.putString("access_token", session.accessToken)
                        settings.putString("refresh_token", session.refreshToken)
                        settings.putString("email_user", _email.value)
                    }

                    // Si es la contraseña por defecto, marcamos que debe cambiarla
                    if (_password.value == "ct1234") {
                        onPassChangeChange()
                    }
                } catch (e: AuthRestException) {
                    // Error de credenciales
                    _loginError.value = true
                    _loginErrorMessage.value = "Credenciales incorrectas"
                    _password.value = ""
                } catch (e: Exception) {
                    // Error general (por ejemplo: no conexión)
                    _loginError.value = true
                    _loginErrorMessage.value = "No se ha podido conectar con la base de datos"
                    _password.value = ""
                }
            }
        }
    }

    /**
     * Comprueba si ya existe una sesión guardada localmente (autologin).
     */
    fun checkSession() {
        _checkSess.value = true
        val settings = Settings()
        val accessToken = settings.getStringOrNull("access_token")
        val refreshToken = settings.getStringOrNull("refresh_token")

        viewModelScope.launch {
            try {
                if (accessToken != null && refreshToken != null) {
                    FullScreenLoadingManager.showLoader()

                    // Recuperamos el usuario a partir del token
                    val user = supabase.auth.retrieveUser(accessToken)

                    // Importamos la sesión manualmente en Supabase SDK
                    val session = UserSession(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresIn = 3600,
                        tokenType = "Bearer",
                        user = user
                    )
                    supabase.auth.importSession(session)

                    // Recuperamos los datos adicionales del empleado en segundo plano
                    withContext(Dispatchers.IO) {
                        Database.getEmployee(user.email ?: "")
                    }
                    _login.value = true
                }
            } catch (e: Exception) {
                _password.value = ""
            } finally {
                FullScreenLoadingManager.hideLoader()
            }
        }
    }

    /**
     * Resetea variables tras cerrar sesión o limpiar el formulario.
     */
    fun resetVar() {
        _visibility.value = false
        _login.value = false
        _password.value = ""
    }

    /**
     * Resetea los errores de login.
     */
    fun resetError() {
        _loginError.value = false
        _loginErrorMessage.value = ""
    }
}
