package com.es.appmovil.database

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

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

    suspend inline fun <reified T : Any> getData(table: String): List<T> {
        try {
            return supabase
                .from(table)
                .select()
                .decodeList<T>()
        }catch (
            e:Exception
        ) {
            println(e)
            return emptyList()
        }
    }

    suspend fun addData(table: String, data: Any) {
        try {
            supabase.from(table).insert(data)
        }catch (
            e:Exception
        ){
            println(e)
        }
    }

    suspend fun updateData(table:String, data:Any, idName:String, id:Any){
        try {
            supabase.from(table).update(data) {
                filter { eq(idName, id) }
            }
        }catch (
            e:Exception
        ){
            println(e)
        }
    }

    suspend fun deregister(table:String, fecha:String, idName:String, id:Any){
        try {
            supabase.from(table).update(
                {
                    set("dateTo", fecha)
                }
            ){
                filter { eq(idName, id) }
            }
        }catch (
            e:Exception
        ){
            println(e)
        }
    }

}