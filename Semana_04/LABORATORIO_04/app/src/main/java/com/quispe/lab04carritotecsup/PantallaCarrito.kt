package com.quispe.lab04carritotecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCarrito() {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var productoEliminar by remember { mutableStateOf<Producto?>(null) }

    val productos = remember { mutableStateListOf<Producto>() }

    val subtotal = productos.sumOf {
        it.precio * it.cantidad
    }

    val igv = subtotal * 0.18

    val total = subtotal + igv

    val descuento = when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }

    val totalConDescuento = total - descuento

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mi Carrito TECSUP",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                val precioProducto = precio.toDoubleOrNull()
                val cantidadProducto = cantidad.toIntOrNull()

                if (
                    nombre.isNotBlank() &&
                    precioProducto != null &&
                    cantidadProducto != null &&
                    precioProducto > 0 &&
                    cantidadProducto > 0
                ) {
                    productos.add(
                        Producto(
                            nombre = nombre,
                            precio = precioProducto,
                            cantidad = cantidadProducto
                        )
                    )

                    nombre = ""
                    precio = ""
                    cantidad = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Productos: ${productos.size}")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos en el carrito"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "S/ ${"%.2f".format(producto.precio)} x ${producto.cantidad}"
                                )

                                Text(
                                    text = "Subtotal: S/ ${
                                        "%.2f".format(
                                            producto.precio * producto.cantidad
                                        )
                                    }"
                                )
                            }

                            Button(
                                onClick = {
                                    productoEliminar = producto
                                }
                            ) {
                                Text("🗑️")
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Subtotal: S/ ${"%.2f".format(subtotal)}"
                )

                Text(
                    text = "IGV 18%: S/ ${"%.2f".format(igv)}"
                )

                if (descuento > 0) {
                    Text(
                        text = "Descuento: -S/ ${"%.2f".format(descuento)}"
                    )
                }

                Text(
                    text = "Total: S/ ${"%.2f".format(totalConDescuento)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoEliminar = null
            },
            title = {
                Text("¿Eliminar este producto?")
            },
            text = {
                Text(producto.nombre)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        productos.remove(producto)
                        productoEliminar = null
                    }
                ) {
                    Text("ELIMINAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        productoEliminar = null
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}