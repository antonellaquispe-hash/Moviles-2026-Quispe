package com.quispe.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.entity.Producto
import com.quispe.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    nombreUsuario: String,
    onProducto: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
    onVerCarrito: () -> Unit = {},
    onVerMisPedidos: () -> Unit = {}
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()
    val cantidad = carrito.sumOf { it.cantidad }
    val total = carrito.sumOf { it.subtotal }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CafeCampus")
                        Text(
                            text = "Bienvenido, $nombreUsuario",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    Button(onClick = onCerrarSesion) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tu cafetería del campus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Descubre algo rico para hoy")
                    }

                    Text(
                        text = "🛒 $cantidad",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Button(
                onClick = onVerMisPedidos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mis pedidos")
            }

            if (carrito.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tu carrito",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("$cantidad producto(s)")
                            Text(
                                text = "S/ ${"%.2f".format(total)}",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onVerCarrito,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver carrito y confirmar")
                        }
                    }
                }
            }

            Text(
                text = "Nuestro menú",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (productos.isEmpty()) {
                Text(
                    text = "Cargando productos...",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 155.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = productos,
                        key = { producto: Producto -> producto.id }
                    ) { producto ->
                        com.quispe.cafecampus.ui.components.ProductoCard(
                            producto = producto,
                            onClick = {
                                onProducto(producto.id)
                            }
                        )
                    }
                }
            }
        }
    }
}