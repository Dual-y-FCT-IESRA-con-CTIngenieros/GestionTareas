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
        supabaseUrl = "https://xyzcompany.supabase.co",
        supabaseKey = "public-anon-key"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }

}