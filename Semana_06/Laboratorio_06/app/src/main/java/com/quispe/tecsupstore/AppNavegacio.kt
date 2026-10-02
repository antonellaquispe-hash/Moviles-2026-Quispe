package com.quispe.tecsupstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupStoreApp() {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    var selectedItem by remember {
        mutableStateOf("Inicio")
    }

    var favoritos by remember {
        mutableStateOf<List<Producto>>(emptyList())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    selectedItem = item

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {

        when (selectedItem) {

            "Inicio" -> {
                PantallaInicio(
                    favoritos = favoritos,
                    onAgregarFavorito = { producto ->

                        if (!favoritos.contains(producto)) {
                            favoritos = favoritos + producto
                        }
                    },
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            "Favoritos" -> {
                PantallaFavoritos(
                    favoritos = favoritos,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            "Mis pedidos" -> {
                PantallaSimple(
                    titulo = "Mis pedidos",
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

            "Perfil" -> {
                PantallaSimple(
                    titulo = "Perfil",
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    favoritos: List<Producto>,
    onAgregarFavorito: (Producto) -> Unit,
    onMenuClick: () -> Unit
) {

    val productos = listOf(
        Producto("Laptop Lenovo", "Tecnología", 2499.90),
        Producto("Mouse Logitech", "Accesorios", 89.90),
        Producto("Teclado Mecánico", "Accesorios", 159.90),
        Producto("Audífonos Sony", "Tecnología", 299.90),
        Producto("Mochila para Laptop", "Otros", 119.90),
        Producto("Memoria USB 64GB", "Tecnología", 39.90)
    )

    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }

    val categorias = listOf(
        "Todos",
        "Tecnología",
        "Accesorios",
        "Otros"
    )

    val productosFiltrados = if (categoriaSeleccionada == "Todos") {
        productos
    } else {
        productos.filter {
            it.categoria == categoriaSeleccionada
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("TECSUP Store")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Text("☰")
                    }
                },
                actions = {
                    Text(
                        text = "♡ ${favoritos.size}",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(
                    vertical = 12.dp
                )
            ) {

                items(categorias) { categoria ->

                    AssistChip(
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = {
                            Text(categoria)
                        }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(productosFiltrados) { producto ->

                    ProductoCard(
                        producto = producto,
                        onFavorito = {
                            onAgregarFavorito(producto)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(
    favoritos: List<Producto>,
    onMenuClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Favoritos")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Text("☰")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (favoritos.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "No tienes productos favoritos.",
                    modifier = Modifier.padding(24.dp)
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(favoritos) { producto ->

                    ProductoCard(
                        producto = producto,
                        onFavorito = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSimple(
    titulo: String,
    onMenuClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(titulo)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick
                    ) {
                        Text("☰")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = titulo,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}