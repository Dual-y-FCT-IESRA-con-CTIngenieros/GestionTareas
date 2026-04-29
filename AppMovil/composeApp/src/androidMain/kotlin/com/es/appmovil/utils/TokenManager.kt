package com.es.appmovil.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.es.appmovil.data.model.LoginResponse

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) = prefs.edit().putString("jwt_token", token).apply()
    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun clearToken() = prefs.edit().remove("jwt_token").apply()

    fun saveUser(user: LoginResponse) {
        prefs.edit()
            .putInt("idEmployee", user.idEmployee)
            .putString("nombre", user.nombre)
            .putString("apellidos", user.apellidos)
            .putString("email", user.email)
            .putInt("idRol", user.idRol)
            .putInt("horasJornada", user.horasJornada)
            .putInt("horasTotalesAnuales", user.horasTotalesAnuales)
            .apply()
    }

    fun getIdEmployee(): Int = prefs.getInt("idEmployee", -1)
    fun getNombre(): String? = prefs.getString("nombre", null)
    fun getApellidos(): String? = prefs.getString("apellidos", null)
    fun getEmail(): String? = prefs.getString("email", null)
    fun getIdRol(): Int = prefs.getInt("idRol", -1)
    /** Horas por jornada del empleado (fallback 8 si aún no se ha guardado). */
    fun getHorasJornada(): Int = prefs.getInt("horasJornada", 8)
    /** Objetivo anual calculado por backend (horasJornada * 224). Fallback 1792. */
    fun getHorasTotalesAnuales(): Int = prefs.getInt("horasTotalesAnuales", 1792)

    fun clearUser() {
        prefs.edit()
            .remove("idEmployee")
            .remove("nombre")
            .remove("apellidos")
            .remove("email")
            .remove("idRol")
            .remove("horasJornada")
            .remove("horasTotalesAnuales")
            .apply()
    }
}

