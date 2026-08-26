package com.quispe.lab02carritokotlin

open class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    open fun obtenerDescripcion(): String {
        return nombre
    }
}

class ProductoTecnologia(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun obtenerDescripcion(): String {
        return "Producto tecnológico: $nombre"
    }
}

class ProductoAccesorio(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun obtenerDescripcion(): String {
        return "Accesorio: $nombre"
    }
}

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0

    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }

    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    val nombreCliente = "Antonella Quispe"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(ProductoTecnologia("Laptop HP", 2500.0, 1))
    carrito.add(ProductoAccesorio("Mouse Logitech", 45.5, 2))
    carrito.add(ProductoAccesorio("Teclado Logitech", 120.0, 1))
    carrito.add(ProductoTecnologia("Monitor LG", 850.0, 1))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
        println(producto.obtenerDescripcion())
    }

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println()
    println("=========================================")
    println("             DETALLE DEL CARRITO")
    println("=========================================")
    println(String.format("%-20s %10s %8s %12s", "Producto", "Precio", "Cant.", "Importe"))
    println("-----------------------------------------")

    for (producto in carrito) {
        val importe = producto.precio * producto.cantidad

        println(
            String.format(
                "%-20s %10.2f %8d %12.2f",
                producto.nombre,
                producto.precio,
                producto.cantidad,
                importe
            )
        )
    }

    println("-----------------------------------------")
    println(String.format("%-30s %12.2f", "Subtotal:", subtotal))
    println(String.format("%-30s %12.2f", "IGV (18%):", igv))
    println(String.format("%-30s %12.2f", "TOTAL:", total))

    val productoMasCaro = carrito.maxByOrNull { it.precio }

    if (productoMasCaro != null) {
        println()
        println("Producto más caro: ${productoMasCaro.nombre}")
        println("Precio: S/ ${String.format("%.2f", productoMasCaro.precio)}")
    }