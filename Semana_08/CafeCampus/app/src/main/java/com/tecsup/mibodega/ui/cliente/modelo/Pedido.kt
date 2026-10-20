package com.tecsup.mibodega.ui.cliente.modelo

data class Pedido(
    val id: Int,
    val productos: List<ItemCarrito>,
    val tipoEntrega: String,
    val costoEntrega: Double,
    val total: Double
)