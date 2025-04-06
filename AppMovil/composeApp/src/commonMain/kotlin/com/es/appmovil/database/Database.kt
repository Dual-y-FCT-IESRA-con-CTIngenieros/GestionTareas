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
        supabaseUrl = "https://vlbcvybmvzmgpncaekua.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYmN2eWJtdnptZ3BuY2Fla3VhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MTk0MTUsImV4cCI6MjA1ODM5NTQxNX0.SokFZe6QPpuqhMo2FAzk1YDwKdc2ZoUv09KL72Uikao"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }

}