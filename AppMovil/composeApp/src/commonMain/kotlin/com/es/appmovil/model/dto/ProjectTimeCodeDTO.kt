package com.es.appmovil.model.dto

/**
 * DTO que asocia un código de tiempo a una lista de proyectos.
 *
 * @property idTimeCode Identificador del código de tiempo.
 * @property projects Lista mutable de identificadores de proyectos asociados.
 */
data class ProjectTimeCodeDTO(
    val idTimeCode:Int,
    val projects:MutableList<String>
)