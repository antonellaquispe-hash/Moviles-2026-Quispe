package com.quispe.lab02carrito

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val carrito = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNombreCliente = findViewById<EditText>(R.id.etNombreCliente)
        val etNombreProducto = findViewById<EditText>(R.id.etNombreProducto)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val etCantidad = findViewById<EditText>(R.id.etCantidad)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        val tvListaProductos = findViewById<TextView>(R.id.tvListaProductos)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Botón para agregar producto
        btnAgregar.setOnClickListener {

            val nombre = etNombreProducto.text.toString().trim()
            val precioStr = etPrecio.text.toString().trim()
            val cantidadStr = etCantidad.text.toString().trim()

            if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
                Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val precio = precioStr.toDoubleOrNull()
            val cantidad = cantidadStr.toIntOrNull()

            if (precio == null || cantidad == null) {
                Toast.makeText(
                    this,
                    "Ingresa valores numéricos válidos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (precio <= 0 || cantidad <= 0) {
                Toast.makeText(
                    this,
                    "Precio y cantidad deben ser mayores a 0",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val producto = Producto(
                nombre = nombre,
                precio = precio,
                cantidad = cantidad
            )

            carrito.add(producto)

            actualizarLista(tvListaProductos)

            // Limpiar campos después de agregar
            etNombreProducto.text.clear()
            etPrecio.text.clear()
            etCantidad.text.clear()

            etNombreProducto.requestFocus()

            Toast.makeText(
                this,
                "Producto agregado",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Botón para calcular el total
        btnCalcular.setOnClickListener {

            if (carrito.isEmpty()) {
                Toast.makeText(
                    this,
                    "Agrega productos primero",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val nombreCliente = etNombreCliente
                .text
                .toString()
                .trim()
                .ifEmpty { "Cliente" }

            val subtotal = calcularSubtotal(carrito)
            val igv = calcularIGV(subtotal)
            val total = calcularTotal(subtotal, igv)

            val descuento = calcularDescuento(total)
            val totalConDescuento = total - descuento

            var resultado = ""

            resultado += "=========================================\n"
            resultado += " CARRITO DE COMPRAS - TIENDA TECSUP\n"
            resultado += "=========================================\n"
            resultado += "Cliente: $nombreCliente\n\n"

            resultado += "--------- DETALLE DEL CARRITO ---------\n"

            var i = 1

            for (p in carrito) {

                val importe = p.precio * p.cantidad

                resultado += String.format(
                    "%d. %-20s x%d S/%8.2f\n",
                    i,
                    p.nombre,
                    p.cantidad,
                    importe
                )

                i++
            }

            resultado += "---------------------------------------\n"

            resultado += "Subtotal: S/ ${
                String.format("%.2f", subtotal)
            }\n"

            resultado += "IGV (18%): S/ ${
                String.format("%.2f", igv)
            }\n"

            resultado += "Total: S/ ${
                String.format("%.2f", total)
            }\n\n"

            // Buscar el producto más caro
            val masCaro = carrito.maxByOrNull { it.precio }

            if (masCaro != null) {

                resultado += "Producto más caro: ${masCaro.nombre} " +
                        "(S/${String.format("%.2f", masCaro.precio)})\n\n"
            }

            // Mostrar descuento si corresponde
            if (descuento > 0) {

                resultado += "¡Descuento aplicado: S/${
                    String.format("%.2f", descuento)
                }!\n"
            }

            resultado += "\nTOTAL CON DESCUENTO: S/${
                String.format("%.2f", totalConDescuento)
            }"

            tvResultado.text = resultado
        }
    }

    // Actualiza la lista de productos agregados
    private fun actualizarLista(tv: TextView) {

        var texto = "Productos agregados (${carrito.size}):\n"

        for ((index, p) in carrito.withIndex()) {

            texto += "${index + 1}. " +
                    "${p.nombre} - " +
                    "S/${String.format("%.2f", p.precio)} " +
                    "x${p.cantidad}\n"
        }

        tv.text = texto
    }
}