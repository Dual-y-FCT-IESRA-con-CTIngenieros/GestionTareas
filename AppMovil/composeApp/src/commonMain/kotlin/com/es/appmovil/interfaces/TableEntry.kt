package com.es.appmovil.interfaces

interface TableEntry {
    fun getFieldMap(): Map<String, Any?>
    fun getId(): String // para identificar cada entrada única
}