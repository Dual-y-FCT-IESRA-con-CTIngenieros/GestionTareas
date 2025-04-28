package com.es.appmovil.database

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singelton con la conexión ha la base de datos supabase que gestiona los datos
 */
object Database {

    // Inicializamos la conexión con la base de datos
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ydbqllrkbfhbiiztytbc.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlkYnFsbHJrYmZoYmlpenR5dGJjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MTgzNDksImV4cCI6MjA1ODM5NDM0OX0.tA9s6ktm3wyPLf4ynZhivu68hkA6JsUBvKeAL6tC9yM"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }
}