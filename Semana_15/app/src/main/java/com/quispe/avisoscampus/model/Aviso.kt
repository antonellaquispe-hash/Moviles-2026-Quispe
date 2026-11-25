package com.quispe.avisoscampus.model

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val urgente: Boolean = false,
    val creadoEn: Long = System.currentTimeMillis()
)

enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}
