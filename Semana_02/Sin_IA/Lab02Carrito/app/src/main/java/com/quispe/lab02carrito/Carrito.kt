package com.quispe.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

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
}