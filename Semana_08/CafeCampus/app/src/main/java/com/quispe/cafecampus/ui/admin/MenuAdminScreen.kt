
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.ui.components.CafeBottomBar
import com.quispe.cafecampus.ui.components.EstadoChip
import com.quispe.cafecampus.ui.components.ProductoIcono
import com.quispe.cafecampus.ui.navigation.ITEMS_ADMIN
import com.quispe.cafecampus.ui.navigation.RutasCafe
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.viewmodel.MenuAdminViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    viewModel: MenuAdminViewModel,
    onVolver: () -> Unit,
    onNuevoProducto: () -> Unit = {},
    onEditarProducto: (Int) -> Unit = {},
    onNavegar: (String) -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    var productoEliminar by remember { mutableStateOf<Producto?>(null) }
    var categoria by remember { mutableStateOf("Todos") }

    val categorias = remember(productos) {
        listOf("Todos") + productos.map { it.categoria }.distinct()
    }

    val productosFiltrados = remember(productos, categoria) {
        if (categoria == "Todos") {
            productos
        } else {
            productos.filter { it.categoria == categoria }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión del menú") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoProducto,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar producto"
                )
            }
        },
        bottomBar = {
            CafeBottomBar(
                items = ITEMS_ADMIN,
                rutaActual = RutasCafe.ADMIN_MENU,
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { item ->
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

            if (productosFiltrados.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Todavía no hay productos registrados.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Usa el botón + para agregar uno.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = 88.dp
                    )
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ProductoAdminCard(
                            producto = producto,
                            onEditar = { onEditarProducto(producto.id) },
                            onEliminar = { productoEliminar = producto }
                        )
                    }
                }
            }
        }
    }

    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Deseas eliminar ${producto.nombre} del menú?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminar(producto)
                        productoEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProductoAdminCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProductoIcono(
                icono = producto.icono,
                tamano = 44.dp,
                conFondo = true
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = String.format(Locale("es", "PE"), "S/ %.2f", producto.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                EstadoChip(
                    estado = if (producto.disponible) {
                        EstadoPedido.LISTO
                    } else {
                        EstadoPedido.CANCELADO
                    },
                    texto = if (producto.disponible) "Disponible" else "Agotado"
                )
            }

            Column {
                IconButton(onClick = onEditar) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar producto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar producto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
