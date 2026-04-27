package com.es.appmovil.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.es.appmovil.data.repository.AuthRepository
import com.es.appmovil.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(error = "Por favor, rellena todos los campos")
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState(loading = true)
            authRepository.login(email, password)
                .onSuccess { response ->
                    tokenManager.saveToken(response.token)
                    tokenManager.saveUser(response)
                    _uiState.value = LoginUiState(success = true)
                }
                .onFailure { e ->
                    _uiState.value = LoginUiState(error = "Credenciales incorrectas o error de red")
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

