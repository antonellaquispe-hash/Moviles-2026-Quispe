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
import androidx.lifecycle.ViewModelProvider
import com.tecsup.rockvault.ui.detalle.DetalleScreen
import com.tecsup.rockvault.ui.detalle.DetalleViewModel
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
        setContent {
            RockVaultTheme {
                var itemSeleccionado by remember { mutableStateOf<String?>(null) }
                BackHandler(enabled = itemSeleccionado != null) {
                    itemSeleccionado = null
                }
                if (itemSeleccionado == null) {
                    val estado by listaViewModel.estado.collectAsState()
                    ListaScreen(
                        estado = estado,
                        onReintentar = listaViewModel::cargar,
                        onItemClick = { id ->
                            itemSeleccionado = id
                            detalleViewModel.cargar(id)
                        }
                    )
                } else {
                    val id = itemSeleccionado.orEmpty()
                    val estado by detalleViewModel.estado.collectAsState()
                    DetalleScreen(
                        estado = estado,
                        onVolver = { itemSeleccionado = null },
                        onReintentar = { detalleViewModel.cargar(id) }
                    )
                }
            }
        }
    }
}


