package com.es.appmovil.database

import com.es.appmovil.model.Config
import com.es.appmovil.model.Employee
import com.es.appmovil.model.EmployeeActivity
import com.es.appmovil.model.dto.EmployeeInsertDTO
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.viewmodel.DataViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/**
 * Singelton con la conexión ha la base de datos supabase que gestiona los datos
 */
object Database {

    // Inicializamos la conexión con la base de datos
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ydbqllrkbfhbiiztytbc.supabase.co/",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlkYnFsbHJrYmZoYmlpenR5dGJjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MTgzNDksImV4cCI6MjA1ODM5NDM0OX0.tA9s6ktm3wyPLf4ynZhivu68hkA6JsUBvKeAL6tC9yM"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }


    suspend fun register(correo: String, contrasenia: String) {
        supabase.auth.signUpWith(Email) {
            email = correo
            password = contrasenia
        }
    }

    suspend inline fun <reified T : Any> getData(table: String): List<T> {
        try {
            return supabase
                .from(table)
                .select()
                .decodeList<T>()
        } catch (
            e: Exception
        ) {
            println(e)
            return emptyList()
        }
    }


    suspend fun getTablesNames(): List<String> {
        return try {
            val response = supabase
                .from("tablas_disponibles")
                .select()

            Json.parseToJsonElement(response.data)
                .jsonArray
                .mapNotNull { it.jsonObject["table_name"]?.toString()?.trim('"') }
                .filter { it != "Config" }

        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    suspend fun getConfigData(clave: String): Config? {
        try {
            return supabase
                .from("Config")
                .select {
                    filter { eq("clave", clave) }
                }
                .decodeSingle()
        } catch (
            e: Exception
        ) {
            println(e)
            return null
        }
    }

    suspend fun getEmployee(email: String) {
        try {
            val employees = supabase.from("Employee").select().decodeList<Employee>()

            DataViewModel.employee = employees.first { it.email == email }

        } catch (
            e: Exception
        ) {
            println(e)
        }

    }

    suspend inline fun <reified T: Any> addData(table: String, data: T) {
        try {
            supabase.from(table).insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun addEmployee(data: EmployeeInsertDTO) {
        try {
            supabase.from("Employee").insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun updateEmployee(data: EmployeeUpdateDTO) {
        try {
            supabase.from("Employee").upsert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun addEmployeeActivity(data: EmployeeActivity) {
        try {
            supabase.from("EmployeeActivity").insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend inline fun <reified T:Any> updateData(table: String, data: T) {
        try {
            println("Datos nuevos: $data")
            supabase.from(table).upsert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun updateEmployeeActivity(data: EmployeeActivity) {
        try {
            supabase.from("EmployeeActivity").upsert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun sendResetPass(email: String) {
        try {
            supabase.auth.resetPasswordForEmail(email)
        } catch (
            _: Exception
        ) {
            println("Error al enviar el correo")
        }
    }

    suspend fun updateUser(newPassword: String) {
        try {
            supabase.auth.updateUser {
                password = newPassword
            }
        } catch (
            _: Exception
        ) {
            println("Error al cambiar la contraseña")
        }
    }


    suspend fun deleteEmployeeActivity(data: EmployeeActivity) {
        try {
            supabase.from("EmployeeActivity").delete() {
                filter {
                    eq("idEmployee", data.idEmployee)
                    eq("idTimeCode", data.idTimeCode)
                    eq("idWorkOrder", data.idWorkOrder)
                    eq("idActivity", data.idActivity)
                    eq("date", data.date)
                }
            }
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    suspend fun deregister(table: String, fecha: String, idName: String, id: Any) {
        try {
            supabase.from(table).update(
                {
                    set("dateTo", fecha)
                }
            ) {
                filter { eq(idName, id) }
            }
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

}