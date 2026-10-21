
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.ui.components.CafeButton

@OptIn(ExperimentalMaterial3Api::class)
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

    val categorias = listOf(
        "Desayunos", "Almuerzos", "Bebidas", "Menús", "Snacks", "Postres"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (producto == null) {
                            "Nuevo producto"
                        } else {
                            "Editar producto"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancelar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre del producto") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descripción del producto") },
                minLines = 2,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio (S/)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = icono,
                onValueChange = { icono = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Icono del producto") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "Categoría",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias.size) { indice ->
                    val item = categorias[indice]

                    FilterChip(
                        selected = categoria == item,
                        onClick = { categoria = item },
                        label = { Text(item) },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor =
                                MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Disponible en el menú",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (disponible) {
                            "Los estudiantes pueden pedirlo"
                        } else {
                            "Marcado como agotado"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = disponible,
                    onCheckedChange = { disponible = it }
                )
            }

            if (error.isNotBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            CafeButton(
                text = "Guardar producto",
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
                }
            )

            CafeButton(
                text = "Cancelar",
                onClick = onCancelar,
                esContorno = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
