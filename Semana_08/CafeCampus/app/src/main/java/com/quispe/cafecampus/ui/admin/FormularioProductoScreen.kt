package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto

@Composable
fun FormularioProductoScreen(
    producto: Producto? = null,
    onGuardar: (Producto) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember(producto) { mutableStateOf(producto?.nombre.orEmpty()) }
    var descripcion by remember(producto) { mutableStateOf(producto?.descripcion.orEmpty()) }
    var categoria by remember(producto) { mutableStateOf(producto?.categoria.orEmpty()) }
    var precio by remember(producto) { mutableStateOf(producto?.precio?.toString().orEmpty()) }
    var icono by remember(producto) { mutableStateOf(producto?.icono ?: "☕") }
    var disponible by remember(producto) { mutableStateOf(producto?.disponible ?: true) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = if (producto == null) "Registrar producto" else "Editar producto",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio en soles") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = icono,
            onValueChange = { icono = it },
            label = { Text("Emoji del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = disponible,
                onCheckedChange = { disponible = it }
            )
            Text("Disponible para los clientes")
        }

        if (error.isNotBlank()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val precioNumerico = precio.toDoubleOrNull()

                if (
                    nombre.isBlank() ||
                    descripcion.isBlank() ||
                    categoria.isBlank() ||
                    icono.isBlank() ||
                    precioNumerico == null ||
                    precioNumerico <= 0
                ) {
                    error = "Completa todos los campos y coloca un precio mayor que cero."
                } else {
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
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar producto")
        }

        TextButton(
            onClick = onCancelar,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cancelar")
        }
    }
}