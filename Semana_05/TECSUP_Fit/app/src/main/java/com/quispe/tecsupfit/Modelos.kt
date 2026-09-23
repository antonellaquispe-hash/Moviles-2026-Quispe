package com.quispe.tecsupfit

data class Clase(
    val nombre: String,
    val horario: String
)

data class Reserva(
    val clase: String,
    val horario: String,
    val estado: String
)