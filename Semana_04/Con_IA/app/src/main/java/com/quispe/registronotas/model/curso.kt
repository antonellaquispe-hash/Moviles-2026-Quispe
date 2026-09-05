package com.quispe.registronotas.model

data class Curso(
    val nombre: String,
    val peso: Int,
    var nota: Float = 0f
)