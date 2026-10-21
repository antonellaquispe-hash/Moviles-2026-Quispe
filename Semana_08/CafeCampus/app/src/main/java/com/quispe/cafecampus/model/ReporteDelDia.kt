
package com.quispe.cafecampus.model

/**
 * Punto del grafico "Pedidos por hora". Se llena con una consulta Room,
 * no con datos fijos.
 */
data class PedidoPorHora(
    val hora: Int,
    val cantidad: Int
)

data class ProductoMasVendido(
    val nombre: String,
    val cantidad: Int
)

data class TotalPorFormaPago(
    val forma: String,
    val total: Double,
    val cantidad: Int
)

data class ReporteDelDia(
    val productoMasVendido: ProductoMasVendido?,
    val totalesPorFormaPago: List<TotalPorFormaPago>,
    val totalVendido: Double,
    val cantidadPedidos: Int
)
