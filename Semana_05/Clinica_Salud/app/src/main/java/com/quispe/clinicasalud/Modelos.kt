package com.quispe.clinicasalud

data class Medico(
    val nombre: String,
    val especialidad: String,
    val valoracion: String
)

data class Cita(
    val medico: String,
    val detalle: String,
    val estado: String
)