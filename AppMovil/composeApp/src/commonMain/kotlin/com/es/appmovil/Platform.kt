package com.es.appmovil

interface Platform {
    val name: String
}


expect fun getPlatform(): Platform