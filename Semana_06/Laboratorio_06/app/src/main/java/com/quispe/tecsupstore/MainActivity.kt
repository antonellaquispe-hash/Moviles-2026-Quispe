package com.quispe.tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TecsupStoreApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupStoreApp() {

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
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 12.dp)
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

                    ProductoCard(producto)
                }
            }
        }
    }
}