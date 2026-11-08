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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.tecsup.rockvault.ui.detalle.DetalleScreen
import com.tecsup.rockvault.ui.detalle.DetalleViewModel
import com.tecsup.rockvault.ui.favoritos.FavoritosScreen
import com.tecsup.rockvault.ui.favoritos.FavoritosViewModel
import com.tecsup.rockvault.ui.lista.ListaScreen
import com.tecsup.rockvault.ui.lista.ListaViewModel
import com.tecsup.rockvault.ui.login.LoginScreen
import com.tecsup.rockvault.ui.login.LoginViewModel
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.components.Cargando
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
        val loginFactory = LoginViewModel.Factory(AppContainer.authRepository)
        val loginViewModel = ViewModelProvider(this, loginFactory)[LoginViewModel::class.java]
        setContent {
            RockVaultTheme {
                val estadoSesion by loginViewModel.estado.collectAsState()
                var itemSeleccionado by remember { mutableStateOf<String?>(null) }
                var pestanaSeleccionada by remember { mutableStateOf(0) }
                BackHandler(enabled = estadoSesion is UiState.Exito && itemSeleccionado != null) {
                    itemSeleccionado = null
                }
                Scaffold(
                    topBar = {
                        if (estadoSesion is UiState.Exito) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("RockVault", modifier = Modifier.padding(vertical = 16.dp))
                                TextButton(onClick = loginViewModel::cerrarSesion) {
                                    Text("Cerrar sesión")
                                }
                            }
                        }
                    },
                    bottomBar = {
                        if (estadoSesion is UiState.Exito && itemSeleccionado == null) {
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
                    when (estadoSesion) {
                        UiState.Cargando -> Cargando(modifier = Modifier.padding(innerPadding))
                        UiState.Vacio, is UiState.Error -> LoginScreen(
                            estado = estadoSesion,
                            onIniciarSesion = loginViewModel::iniciarSesion,
                            onReintentar = loginViewModel::verificarSesion,
                            modifier = Modifier.padding(innerPadding)
                        )
                        is UiState.Exito -> if (itemSeleccionado == null && pestanaSeleccionada == 0) {
                        val estado by listaViewModel.estado.collectAsState()
                        val aviso by listaViewModel.aviso.collectAsState()
                        ListaScreen(
                            estado = estado,
                            aviso = aviso,
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
}


