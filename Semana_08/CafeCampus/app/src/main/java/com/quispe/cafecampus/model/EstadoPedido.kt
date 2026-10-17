package com.quispe.cafecampus.model

enum class EstadoPedido {
    RECIBIDO,
    PREPARANDO,
    LISTO,
    ENTREGADO,
    CANCELADO;

    fun siguiente(): EstadoPedido? {
        return when (this) {
            RECIBIDO -> PREPARANDO
            PREPARANDO -> LISTO
            LISTO -> ENTREGADO
            ENTREGADO, CANCELADO -> null
        }
    }

    fun puedeAvanzar(): Boolean = siguiente() != null

    fun puedeCancelarse(): Boolean {
        return this == RECIBIDO || this == PREPARANDO || this == LISTO
    }

    fun etiqueta(): String {
        return when (this) {
            RECIBIDO -> "Recibido"
            PREPARANDO -> "Preparando"
            LISTO -> "Listo"
            ENTREGADO -> "Entregado"
            CANCELADO -> "Cancelado"
        }
    }
}