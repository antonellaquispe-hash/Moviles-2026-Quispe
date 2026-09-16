package com.quispe.lab04carritotecsup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

    val productos = remember { mutableStateListOf<Producto>() }

    val subtotal = productos.sumOf {
        it.precio * it.cantidad
    }

    val igv = subtotal * 0.18

    val total = subtotal + igv

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mi Carrito TECSUP",
            style = MaterialTheme.typography.headlineMedium
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        androidx.compose.foundation.layout.Spacer(
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

        androidx.compose.foundation.layout.Spacer(
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

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Productos: ${productos.size}")

        androidx.compose.foundation.layout.Spacer(
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
                                    text = "Precio: S/ ${
                                        "%.2f".format(producto.precio)
                                    }"
                                )

                                Text(
                                    text = "Cantidad: ${producto.cantidad}"
                                )
                            }

                            Button(
                                onClick = {
                                    productos.remove(producto)
                                }
                            ) {
                                Text("ELIMINAR")
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

                Text(
                    text = "Total: S/ ${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}