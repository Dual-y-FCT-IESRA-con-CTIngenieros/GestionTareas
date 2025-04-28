package com.es.appmovil.model.dto

import com.es.appmovil.model.Project

data class ProjectTimeCodeDTO(
    val idTimeCode:Int,
    val projects:MutableList<String>
)
