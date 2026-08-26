package com.quispe.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

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

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        // %-20s: 20 espacios, alineado a la izquierda
        // %8.2f: 8 espacios totales, con 2 decimales, alineado a la derecha
        println(String.format("%d. %-20s x%d  S/%8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    val nombreCliente = "Antonella Quispe"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Teclado Logitech", 120.0, 1))
    carrito.add(Producto("Monitor LG", 850.0, 1))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    mostrarDetalle(carrito)

    println("Cantidad de productos: ${carrito.size}")
    println()


    println(String.format("%-25s S/%8.2f", "Subtotal:", subtotal))
    println(String.format("%-25s S/%8.2f", "IGV (18%):", igv))
    println(String.format("%-25s S/%8.2f", "TOTAL A PAGAR:", total))
}