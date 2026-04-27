package com.es.appmovil.data.repository

import com.es.appmovil.data.model.LoginRequest
import com.es.appmovil.data.model.LoginResponse
import com.es.appmovil.data.remote.ApiService

class AuthRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

