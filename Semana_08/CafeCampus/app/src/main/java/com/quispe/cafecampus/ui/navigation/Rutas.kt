package com.quispe.cafecampus.ui.navigation

object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val ADMIN = "admin"

    fun detalleProducto(productoId: Int): String {
        return "detalle/$productoId"
    }
}