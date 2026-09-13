package com.quispe.manejoestadosconia

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false
)