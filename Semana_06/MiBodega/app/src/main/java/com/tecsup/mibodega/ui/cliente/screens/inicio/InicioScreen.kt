package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var textoBusqueda by remember { mutableStateOf("") }
    var ordenAscendente by remember { mutableStateOf(true) }

    val productosFiltrados = productos
        .filter { producto ->
            categoriaSeleccionada == "Todos" ||
                    producto.categoria == categoriaSeleccionada
        }
        .filter { producto ->
            producto.nombre.contains(textoBusqueda, ignoreCase = true)
        }
        .sortedBy {
            if (ordenAscendente) it.precio else -it.precio
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mi Bodega",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Compra fácil y rápido",
                            fontSize = 12.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge {
                                        Text(cantidadCarrito.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            BarraInferior()
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Qué estás buscando?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Encuentra tus productos favoritos",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar productos...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Categorías",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                TextButton(
                    onClick = {
                        ordenAscendente = !ordenAscendente
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Ordenar"
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = if (ordenAscendente) {
                            "Menor precio"
                        } else {
                            "Mayor precio"
                        }
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaCategorias) { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = {
                            Text(categoria)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Productos",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${productosFiltrados.size} encontrados",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (productosFiltrados.isEmpty()) {
                SinResultados()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosFiltrados) { producto ->
                        ProductoInicioCard(
                            producto = producto,
                            onClick = {
                                onProductoClick(producto)
                            },
                            onAgregar = {
                                onAgregarProducto(producto)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoInicioCard(
    producto: Producto,
    onClick: () -> Unit,
    onAgregar: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛒",
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = producto.categoria,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "S/ %.2f".format(producto.precio),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                TextButton(
                    onClick = onAgregar
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}

@Composable
private fun SinResultados() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🔎",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "No encontramos productos",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Prueba con otra búsqueda o categoría",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BarraInferior() {
    var seleccionado by remember { mutableStateOf(0) }

    NavigationBar {
        NavigationBarItem(
            selected = seleccionado == 0,
            onClick = { seleccionado = 0 },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Inicio"
                )
            },
            label = {
                Text("Inicio")
            }
        )

        NavigationBarItem(
            selected = seleccionado == 1,
            onClick = { seleccionado = 1 },
            icon = {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Categorías"
                )
            },
            label = {
                Text("Categorías")
            }
        )

        NavigationBarItem(
            selected = seleccionado == 2,
            onClick = { seleccionado = 2 },
            icon = {
                Icon(
                    Icons.Default.Receipt,
                    contentDescription = "Pedidos"
                )
            },
            label = {
                Text("Pedidos")
            }
        )

        NavigationBarItem(
            selected = seleccionado == 3,
            onClick = { seleccionado = 3 },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Perfil"
                )
            },
            label = {
                Text("Perfil")
            }
        )
    }
}