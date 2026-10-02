package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake

@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onVerFavoritos: () -> Unit,
    onVerPedidos: () -> Unit,
    onVerPerfil: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    var textoBusqueda by remember {
        mutableStateOf("")
    }

    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }

    var ordenarAscendente by remember {
        mutableStateOf(true)
    }

    val productosFiltrados = productos
        .filter { producto ->
            categoriaSeleccionada == "Todos" ||
                    producto.categoria == categoriaSeleccionada
        }
        .filter { producto ->
            producto.nombre.contains(
                textoBusqueda,
                ignoreCase = true
            ) ||
                    producto.descripcion.contains(
                        textoBusqueda,
                        ignoreCase = true
                    )
        }
        .sortedBy { producto ->
            if (ordenarAscendente) {
                producto.precio
            } else {
                -producto.precio
            }
        }

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Mi Bodega",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onVerPerfil
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil"
                    )
                }

                IconButton(
                    onClick = onVerPedidos
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Mis pedidos"
                    )
                }

                IconButton(
                    onClick = onVerFavoritos
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favoritos"
                    )
                }

                BadgedBox(
                    badge = {
                        if (cantidadCarrito > 0) {
                            Badge {
                                Text(
                                    text = cantidadCarrito.toString()
                                )
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onVerCarrito
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = {
                        textoBusqueda = it
                    },
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
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            rememberScrollState()
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    listaCategorias.forEach { categoria ->

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

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Productos",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            ordenarAscendente = !ordenarAscendente
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Ordenar por precio"
                        )
                    }

                    Text(
                        text = if (ordenarAscendente) {
                            "Menor precio"
                        } else {
                            "Mayor precio"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (productosFiltrados.isEmpty()) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se encontraron productos."
                        )
                    }

                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 20.dp
                        ),
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
}

@Composable
private fun ProductoInicioCard(
    producto: Producto,
    onClick: () -> Unit,
    onAgregar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = producto.categoria
            )

            Text(
                text = producto.descripcion,
                maxLines = 2
            )

            Text(
                text = "S/ %.2f".format(producto.precio),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onAgregar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Agregar al carrito"
                )
            }
        }
    }
}