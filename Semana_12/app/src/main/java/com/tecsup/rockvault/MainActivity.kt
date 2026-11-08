package com.tecsup.rockvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tecsup.rockvault.ui.detalle.DetalleScreen
import com.tecsup.rockvault.ui.detalle.DetalleViewModel
import com.tecsup.rockvault.ui.favoritos.FavoritosScreen
import com.tecsup.rockvault.ui.favoritos.FavoritosViewModel
import com.tecsup.rockvault.ui.lista.ListaScreen
import com.tecsup.rockvault.ui.lista.ListaViewModel
import com.tecsup.rockvault.ui.theme.RockVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = AppContainer.itemRepository(applicationContext)
        val listaFactory = ListaViewModel.Factory(repository)
        val listaViewModel = ViewModelProvider(this, listaFactory)[ListaViewModel::class.java]
        val detalleFactory = DetalleViewModel.Factory(repository)
        val detalleViewModel = ViewModelProvider(this, detalleFactory)[DetalleViewModel::class.java]
        val favoritosFactory = FavoritosViewModel.Factory(repository)
        val favoritosViewModel = ViewModelProvider(this, favoritosFactory)[FavoritosViewModel::class.java]
        setContent {
            RockVaultTheme {
                var itemSeleccionado by remember { mutableStateOf<String?>(null) }
                var pestanaSeleccionada by remember { mutableStateOf(0) }
                BackHandler(enabled = itemSeleccionado != null) {
                    itemSeleccionado = null
                }
                Scaffold(
                    bottomBar = {
                        if (itemSeleccionado == null) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = pestanaSeleccionada == 0,
                                    onClick = { pestanaSeleccionada = 0 },
                                    icon = { Text("♫") },
                                    label = { Text("Catálogo") }
                                )
                                NavigationBarItem(
                                    selected = pestanaSeleccionada == 1,
                                    onClick = { pestanaSeleccionada = 1 },
                                    icon = { Text("♥") },
                                    label = { Text("Favoritos") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    if (itemSeleccionado == null && pestanaSeleccionada == 0) {
                        val estado by listaViewModel.estado.collectAsState()
                        ListaScreen(
                            estado = estado,
                            onReintentar = listaViewModel::cargar,
                            onItemClick = { id ->
                                itemSeleccionado = id
                                detalleViewModel.cargar(id)
                            },
                            onAlternarFavorito = listaViewModel::alternarFavorito,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else if (itemSeleccionado == null) {
                        val estado by favoritosViewModel.estado.collectAsState()
                        FavoritosScreen(
                            estado = estado,
                            onItemClick = { id ->
                                itemSeleccionado = id
                                detalleViewModel.cargar(id)
                            },
                            onAlternarFavorito = favoritosViewModel::alternarFavorito,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        val id = itemSeleccionado.orEmpty()
                        val estado by detalleViewModel.estado.collectAsState()
                        DetalleScreen(
                            estado = estado,
                            onVolver = { itemSeleccionado = null },
                            onReintentar = { detalleViewModel.cargar(id) },
                            onAlternarFavorito = detalleViewModel::alternarFavorito,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}


