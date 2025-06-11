package com.es.appmovil.interfaces

/**
 * Interfaz que representa una entrada genérica en una tabla de base de datos.
 *
 * Las clases que implementan esta interfaz deben proporcionar un identificador único
 * y una representación de sus campos en forma de mapa clave-valor.
 */
interface TableEntry {
    
    /**
     * Devuelve un mapa que representa los campos de la entrada y sus valores.
     *
     * @return Un [Map] donde la clave es el nombre del campo y el valor es su contenido (puede ser `null`).
     */
    fun getFieldMap(): Map<String, Any?>

    /**
     * Devuelve el identificador único de esta entrada.
     *
     * @return Un [String] que representa el ID único de la entrada.
     */
    fun getId(): String // para identificar cada entrada única
}