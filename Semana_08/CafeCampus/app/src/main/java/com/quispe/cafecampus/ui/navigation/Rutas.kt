package com.quispe.cafecampus.ui.navigation

object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val ADMIN = "admin"
    const val CONFIRMAR_PEDIDO = "confirmar-pedido"
    const val SEGUIMIENTO = "seguimiento/{pedidoId}"
    const val MIS_PEDIDOS = "mis-pedidos"

    fun detalleProducto(productoId: Int): String {
        return "detalle/$productoId"
    }

    fun seguimientoPedido(pedidoId: Int): String {
        return "seguimiento/$pedidoId"
    }
}