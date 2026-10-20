package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.viewmodel.MenuAdminViewModel
import java.util.Locale

@Composable
fun MenuAdminScreen(
    viewModel: MenuAdminViewModel,
    onVolver: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    var mostrarFormulario by remember { mutableStateOf(false) }
    var productoEditar by remember { mutableStateOf<Producto?>(null) }
    var productoEliminar by remember { mutableStateOf<Producto?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión del menú",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        productoEditar = null
                        mostrarFormulario = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Nuevo producto")
                }

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Volver")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (productos.isEmpty()) {
                Text("Todavía no hay productos registrados.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productos, key = { it.id }) { producto ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${producto.icono} ${producto.nombre}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(producto.descripcion)
                                Text("Categoría: ${producto.categoria}")
                                Text(
                                    String.format(
                                        Locale("es", "PE"),
                                        "Precio: S/ %.2f",
                                        producto.precio
                                    )
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = producto.disponible,
                                        onCheckedChange = {
                                            viewModel.cambiarDisponibilidad(
                                                producto,
                                                it
                                            )
                                        }
                                    )
                                    Text(
                                        if (producto.disponible) {
                                            "Disponible"
                                        } else {
                                            "No disponible"
                                        }
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            productoEditar = producto
                                            mostrarFormulario = true
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }

                                    Button(
                                        onClick = {
                                            productoEliminar = producto
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        FormularioProductoDialog(
            producto = productoEditar,
            onCerrar = {
                mostrarFormulario = false
                productoEditar = null
            },
            onGuardar = { producto ->
                viewModel.guardarProducto(producto)
                mostrarFormulario = false
                productoEditar = null
            }
        )
    }

    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoEliminar = null },
            title = { Text("Eliminar producto") },
            text = {
                Text("¿Deseas eliminar ${producto.nombre} del menú?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarProducto(producto)
                        productoEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { productoEliminar = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun FormularioProductoDialog(
    producto: Producto?,
    onCerrar: () -> Unit,
    onGuardar: (Producto) -> Unit
) {
    var nombre by remember(producto) {
        mutableStateOf(producto?.nombre.orEmpty())
    }
    var descripcion by remember(producto) {
        mutableStateOf(producto?.descripcion.orEmpty())
    }
    var categoria by remember(producto) {
        mutableStateOf(producto?.categoria.orEmpty())
    }
    var precio by remember(producto) {
        mutableStateOf(producto?.precio?.toString().orEmpty())
    }
    var icono by remember(producto) {
        mutableStateOf(producto?.icono ?: "☕")
    }
    var disponible by remember(producto) {
        mutableStateOf(producto?.disponible ?: true)
    }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = {
            Text(if (producto == null) "Nuevo producto" else "Editar producto")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = icono,
                    onValueChange = { icono = it },
                    label = { Text("Emoji del producto") },
                    singleLine = true
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = disponible,
                        onCheckedChange = { disponible = it }
                    )
                    Text("Disponible")
                }
                if (error) {
                    Text(
                        text = "Completa los campos y coloca un precio válido mayor que cero.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val precioNumerico = precio.toDoubleOrNull()
                    if (
                        nombre.isNotBlank() &&
                        descripcion.isNotBlank() &&
                        categoria.isNotBlank() &&
                        icono.isNotBlank() &&
                        precioNumerico != null &&
                        precioNumerico > 0
                    ) {
                        onGuardar(
                            Producto(
                                id = producto?.id ?: 0,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoria.trim(),
                                precio = precioNumerico,
                                disponible = disponible,
                                icono = icono.trim()
                            )
                        )
                    } else {
                        error = true
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text("Cancelar")
            }
        }
    )
}