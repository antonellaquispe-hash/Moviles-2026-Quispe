package com.quispe.calculadoracuotas

import java.text.SimpleDateFormat
import java.util.*

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

object CalculadoraCuotas {

    fun obtenerTasaInteres(numCuotas: Int): Double {
        return when (numCuotas) {
            in 1..6 -> 0.20
            in 7..12 -> 0.40
            in 13..24 -> 0.60
            else -> 0.0
        }
    }

    fun calcularYMostrarEnConsola(
        productos: List<Producto>,
        numCuotas: Int
    ) {
        val montoInicial = productos.sumOf { it.precio * it.cantidad }
        val tasaInteres = obtenerTasaInteres(numCuotas)
        val montoIntereses = montoInicial * tasaInteres
        val montoTotalPagar = montoInicial + montoIntereses
        val pagoMensual = montoTotalPagar / numCuotas

        println()
        println("==================================================")
        println("              RESUMEN DE COMPRA")
        println("==================================================")

        productos.forEachIndexed { index, producto ->
            val subtotal = producto.precio * producto.cantidad

            println("${index + 1}. ${producto.nombre}")
            println("   Precio unitario: S/ ${String.format("%.2f", producto.precio)}")
            println("   Cantidad: ${producto.cantidad}")
            println("   Subtotal: S/ ${String.format("%.2f", subtotal)}")
            println()
        }

        println("Monto inicial: S/ ${String.format("%.2f", montoInicial)}")
        println("Número de cuotas: $numCuotas")
        println("Interés aplicado: ${(tasaInteres * 100).toInt()}%")
        println("Monto de intereses: S/ ${String.format("%.2f", montoIntereses)}")
        println("Monto total a pagar: S/ ${String.format("%.2f", montoTotalPagar)}")
        println("Pago mensual: S/ ${String.format("%.2f", pagoMensual)}")

        println("==================================================")
        println("              FECHAS DE PAGO")
        println("==================================================")

        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        for (i in 1..numCuotas) {
            calendario.add(Calendar.MONTH, 1)
            val fechaPago = formatoFecha.format(calendario.time)
            println("Cuota $i - $fechaPago - S/ ${String.format("%.2f", pagoMensual)}")
        }

        println("==================================================")
    }

    @JvmStatic
    fun main(args: Array<String>) {

        println("==================================================")
        println("             CALCULADORA DE CUOTAS")
        println("==================================================")

        val productos = mutableListOf<Producto>()

        while (true) {
            println()
            println("PRODUCTO ${productos.size + 1}")
            println("------------------------------------------")

            print("Ingrese nombre del producto: ")
            val nombreProducto = readLine()?.trim() ?: ""

            if (nombreProducto.isEmpty()) {
                println("Error: debe ingresar el nombre del producto.")
                continue
            }

            print("Ingrese precio del producto: ")
            val precio = readLine()?.toDoubleOrNull() ?: 0.0

            if (precio <= 0) {
                println("Error: el precio debe ser mayor que 0.")
                continue
            }

            print("Ingrese cantidad: ")
            val cantidad = readLine()?.toIntOrNull() ?: 0

            if (cantidad <= 0) {
                println("Error: la cantidad debe ser mayor que 0.")
                continue
            }

            productos.add(
                Producto(
                    nombreProducto,
                    precio,
                    cantidad
                )
            )

            println()
            print("¿Desea agregar otro producto? (s/n): ")
            val respuesta = readLine()?.trim()?.lowercase()

            if (respuesta != "s") {
                break
            }
        }

        if (productos.isEmpty()) {
            println("No se agregaron productos.")
            return
        }

        println()
        print("Ingrese número de cuotas (1-24): ")
        val numCuotas = readLine()?.toIntOrNull() ?: 0

        if (numCuotas !in 1..24) {
            println("Error: el número de cuotas debe estar entre 1 y 24.")
            return
        }

        calcularYMostrarEnConsola(
            productos,
            numCuotas
        )
    }

}
