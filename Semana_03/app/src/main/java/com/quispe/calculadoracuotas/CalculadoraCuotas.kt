package com.quispe.calculadoracuotas

import java.text.SimpleDateFormat
import java.util.*

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
        nombreProducto: String,
        precio: Double,
        cantidad: Int,
        numCuotas: Int
    ) {
        val montoInicial = precio * cantidad
        val tasaInteres = obtenerTasaInteres(numCuotas)
        val montoIntereses = montoInicial * tasaInteres
        val montoTotalPagar = montoInicial + montoIntereses
        val pagoMensual = montoTotalPagar / numCuotas

        println()
        println("==================================================")
        println("              RESUMEN DE COMPRA")
        println("==================================================")
        println("Producto: $nombreProducto")
        println("Precio unitario: S/ ${String.format("%.2f", precio)}")
        println("Cantidad: $cantidad")
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

        print("Ingrese nombre del producto: ")
        val nombreProducto = readLine()?.trim() ?: ""

        print("Ingrese precio del producto: ")
        val precio = readLine()?.toDoubleOrNull() ?: 0.0

        print("Ingrese cantidad: ")
        val cantidad = readLine()?.toIntOrNull() ?: 0

        print("Ingrese número de cuotas (1-24): ")
        val numCuotas = readLine()?.toIntOrNull() ?: 0

        if (nombreProducto.isEmpty()) {
            println("Error: debe ingresar el nombre del producto.")
            return
        }

        if (precio <= 0) {
            println("Error: el precio debe ser mayor que 0.")
            return
        }

        if (cantidad <= 0) {
            println("Error: la cantidad debe ser mayor que 0.")
            return
        }

        if (numCuotas !in 1..24) {
            println("Error: el número de cuotas debe estar entre 1 y 24.")
            return
        }

        calcularYMostrarEnConsola(
            nombreProducto,
            precio,
            cantidad,
            numCuotas
        )
    }
}