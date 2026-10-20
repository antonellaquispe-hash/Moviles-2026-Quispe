package com.quispe.cafecampus.model

import com.quispe.cafecampus.data.local.entity.Producto

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int,
    val nota: String = ""
) {
    val subtotal: Double
        get() = producto.precio * cantidad
}