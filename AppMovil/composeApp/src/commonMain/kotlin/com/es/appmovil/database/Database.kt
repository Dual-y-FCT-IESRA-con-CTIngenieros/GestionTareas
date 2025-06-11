package com.es.appmovil.database

import com.es.appmovil.model.Calendar
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

    /**
     * Cliente de Supabase configurado con autenticación y acceso a PostgREST.
     */
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ydbqllrkbfhbiiztytbc.supabase.co/",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlkYnFsbHJrYmZoYmlpenR5dGJjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MTgzNDksImV4cCI6MjA1ODM5NDM0OX0.tA9s6ktm3wyPLf4ynZhivu68hkA6JsUBvKeAL6tC9yM"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }

    /**
     * Registra un nuevo usuario con correo y contraseña mediante autenticación de Supabase.
     *
     * @param correo Dirección de correo electrónico del nuevo usuario.
     * @param contrasenia Contraseña para la cuenta del usuario.
     */
    suspend fun register(correo: String, contrasenia: String) {
        supabase.auth.signUpWith(Email) {
            email = correo
            password = contrasenia
        }
    }

    /**
     * Obtiene datos de una tabla de Supabase y los decodifica como una lista del tipo especificado.
     *
     * @param T Tipo de datos esperado.
     * @param table Nombre de la tabla a consultar.
     * @return Lista de elementos del tipo [T], o vacía si ocurre un error.
     */
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

    /**
     * Obtiene una lista de nombres de tablas disponibles excluyendo la tabla "Config".
     *
     * @return Lista de nombres de tablas como [String].
     */
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

    /**
     * Obtiene un valor de configuración por su clave.
     *
     * @param clave Clave identificadora de la configuración.
     * @return Objeto [Config] o `null` si no se encuentra.
     */
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

    /**
     * Obtiene los datos de un empleado por correo electrónico y los asigna a [DataViewModel.employee].
     *
     * @param email Correo del empleado a buscar.
     */
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

    /**
     * Inserta un nuevo registro en la tabla especificada.
     *
     * @param T Tipo de datos del objeto a insertar.
     * @param table Nombre de la tabla.
     * @param data Datos a insertar.
     */
    suspend inline fun <reified T: Any> addData(table: String, data: T) {

        try {
            supabase.from(table).insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    /**
     * Inserta un nuevo empleado en la tabla "Employee".
     *
     * @param data Datos del empleado en formato [EmployeeInsertDTO].
     */
    suspend fun addEmployee(data: EmployeeInsertDTO) {
        try {
            supabase.from("Employee").insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    /**
     * Actualiza o inserta (upsert) un empleado en la tabla "Employee".
     *
     * @param data Datos actualizados del empleado en formato [EmployeeUpdateDTO].
     */
    suspend fun updateEmployee(data: EmployeeUpdateDTO) {
        try {
            supabase.from("Employee").upsert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    /**
     * Inserta una nueva actividad del empleado en la tabla "EmployeeActivity".
     *
     * @param data Datos de la actividad del empleado.
     */
    suspend fun addEmployeeActivity(data: EmployeeActivity) {
        try {
            supabase.from("EmployeeActivity").insert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    /**
     * Realiza un upsert genérico sobre la tabla especificada.
     *
     * @param T Tipo de los datos.
     * @param table Nombre de la tabla.
     * @param data Datos a actualizar o insertar.
     */
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

    /**
     * Actualiza o inserta una actividad del empleado.
     *
     * @param data Datos de la actividad.
     */
    suspend fun updateEmployeeActivity(data: EmployeeActivity) {
        try {
            supabase.from("EmployeeActivity").upsert(data)
        } catch (
            e: Exception
        ) {
            println(e)
        }
    }

    /**
     * Elimina un registro de calendario con el ID y fecha especificados.
     *
     * @param table Nombre de la tabla.
     * @param data Objeto [Calendar] con los datos a eliminar.
     */
    suspend fun deleteCalendar(table:String, data: Calendar){
        try {
            supabase.from(table).delete {
                filter {
                    eq("idCalendar", data.idCalendar)
                    eq("date", data.date)
                }
            }
        }catch (
            e:Exception
        ){
            println(e)
        }
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * @param newPassword Nueva contraseña a establecer.
     */
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

    /**
     * Elimina una actividad del empleado con base en varias condiciones.
     *
     * @param data Actividad del empleado a eliminar.
     */
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

}