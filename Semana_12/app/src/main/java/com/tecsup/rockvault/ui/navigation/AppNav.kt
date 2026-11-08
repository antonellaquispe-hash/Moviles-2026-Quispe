package com.tecsup.rockvault.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.domain.model.Rol
import com.tecsup.rockvault.domain.model.Sesion
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.admin.AdminScreen
import com.tecsup.rockvault.ui.admin.ResumenAdmin
import com.tecsup.rockvault.ui.detalle.DetalleScreen
import com.tecsup.rockvault.ui.favoritos.FavoritosScreen
import com.tecsup.rockvault.ui.lista.ListaScreen

private enum class DestinoApp {
    CATALOGO,
    FAVORITOS,
    ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(
    sesion: Sesion,
    estadoLista: UiState<List<Item>>,
    avisoLista: String?,
    estadoDetalle: UiState<Item>,
    estadoFavoritos: UiState<List<Item>>,
    estadoAdmin: UiState<ResumenAdmin>,
    mensajeAdmin: String?,
    onReintentarLista: () -> Unit,
    onCargarDetalle: (String) -> Unit,
    onAlternarFavoritoLista: (String) -> Unit,
    onAlternarFavoritoFavoritos: (String) -> Unit,
    onAlternarFavoritoDetalle: () -> Unit,
    onSincronizar: () -> Unit,
    onLimpiarCache: () -> Unit,
    onCargarTotales: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val esAdmin = sesion.rol == Rol.ADMIN
    var destino by remember { mutableStateOf(DestinoApp.CATALOGO) }
    var itemSeleccionado by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = itemSeleccionado != null) {
        itemSeleccionado = null
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            when {
                                itemSeleccionado != null -> "Detalle"
                                destino == DestinoApp.FAVORITOS -> "Favoritos"
                                destino == DestinoApp.ADMIN -> "Administración"
                                else -> "RockVault"
                            }
                        )
                    },
                    actions = {
                        TextButton(onClick = onCerrarSesion) {
                            Text("Salir", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (itemSeleccionado == null) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        selected = destino == DestinoApp.CATALOGO,
                        onClick = { destino = DestinoApp.CATALOGO },
                        icon = { Text("▦") },
                        label = { Text("Lecturas") }
                    )
                    NavigationBarItem(
                        selected = destino == DestinoApp.FAVORITOS,
                        onClick = { destino = DestinoApp.FAVORITOS },
                        icon = { Text("♥") },
                        label = { Text("Favoritos") }
                    )
                    if (esAdmin) {
                        NavigationBarItem(
                            selected = destino == DestinoApp.ADMIN,
                            onClick = {
                                destino = DestinoApp.ADMIN
                                onCargarTotales()
                            },
                            icon = { Text("⚙") },
                            label = { Text("Admin") }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when {
            itemSeleccionado != null -> {
                DetalleScreen(
                    estado = estadoDetalle,
                    onVolver = { itemSeleccionado = null },
                    onReintentar = { itemSeleccionado?.let(onCargarDetalle) },
                    onAlternarFavorito = onAlternarFavoritoDetalle,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            destino == DestinoApp.FAVORITOS -> FavoritosScreen(
                estado = estadoFavoritos,
                onItemClick = { id ->
                    itemSeleccionado = id
                    onCargarDetalle(id)
                },
                onAlternarFavorito = onAlternarFavoritoFavoritos,
                modifier = Modifier.padding(innerPadding)
            )
            esAdmin && destino == DestinoApp.ADMIN -> AdminScreen(
                estado = estadoAdmin,
                mensaje = mensajeAdmin,
                onSincronizar = onSincronizar,
                onLimpiarCache = onLimpiarCache,
                onReintentar = onCargarTotales,
                modifier = Modifier.padding(innerPadding)
            )
            else -> ListaScreen(
                estado = estadoLista,
                aviso = avisoLista,
                onReintentar = onReintentarLista,
                onItemClick = { id ->
                    itemSeleccionado = id
                    onCargarDetalle(id)
                },
                onAlternarFavorito = onAlternarFavoritoLista,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
