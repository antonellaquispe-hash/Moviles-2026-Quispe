package com.quispe.cafecampus.ui.navigation

object RutasCafe {
    const val LOGIN = "login"
    const val SPLASH = "splash"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val ADMIN = "admin"
    const val ADMIN_MENU = "admin/menu"
    const val ADMIN_PEDIDOS = "admin/pedidos"
    const val ADMIN_DETALLE_PEDIDO = "admin/pedido/{pedidoId}"
    const val ADMIN_AJUSTES = "admin/ajustes"
    const val ADMIN_FORMULARIO_PRODUCTO = "admin/producto/{productoId}"
    const val ADMIN_NUEVO_PRODUCTO = "admin/producto/nuevo"
    const val CONFIRMAR_PEDIDO = "confirmar-pedido"
    const val SEGUIMIENTO = "seguimiento/{pedidoId}"
    const val MIS_PEDIDOS = "mis-pedidos"
    const val CLIENTE_PERFIL = "cliente/perfil"

    fun detalleProducto(productoId: Int): String {
        return "detalle/$productoId"
    }

    fun seguimientoPedido(pedidoId: Int): String {
        return "seguimiento/$pedidoId"
    }

    fun detallePedidoAdmin(pedidoId: Int): String {
        return "admin/pedido/$pedidoId"
    }

    fun formularioProducto(productoId: Int): String {
        return "admin/producto/$productoId"
    }
}
