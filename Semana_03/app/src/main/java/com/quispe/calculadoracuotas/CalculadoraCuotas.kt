package com.quispe.calculadoracuotas

import java.text.SimpleDateFormat
import java.util.*

object CalculadoraCuotas {

    fun obtenerTasaInteres(numCuotas: Int): Double {
        return when (numCuotas) {
            in 1..6 -> 0.20
            in 7..12 -> 0.40
            in 13..24 -> 0.60
            else -> 0.60
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

        println("==================================================")
        println(" RESUMEN DE COMPRA: $nombreProducto")
        println("==================================================")
        println("Precio unitario: S/ $precio")
        println("Cantidad: $cantidad")
        println("Monto Inicial (Costo principal): S/ ${String.format("%.2f", montoInicial)}")
        println("Número de cuotas: $numCuotas")
        println("Tasa de interés aplicada: ${(tasaInteres * 100).toInt()}%")
        println("Monto total a pagar (con intereses): S/ ${String.format("%.2f", montoTotalPagar)}")
        println("Pago mensual estimado: S/ ${String.format("%.2f", pagoMensual)}")
        println("==================================================")
        println(" FECHAS DE PAGO ESTIMADAS:")

        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        for (i in 1..numCuotas) {
            calendario.add(Calendar.MONTH, 1)
            val fechaPago = formatoFecha.format(calendario.time)
            println(" -> Cuota $i: S/ ${String.format("%.2f", pagoMensual)} | Fecha: $fechaPago")
        }
        println("==================================================")
    }
}