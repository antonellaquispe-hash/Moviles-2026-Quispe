
package com.quispe.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.cafecampus.data.local.CafeDatabase
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepositoryRoom
import com.quispe.cafecampus.model.Rol
import com.quispe.cafecampus.ui.navigation.NavCafe
import com.quispe.cafecampus.ui.theme.CafeTheme
import com.quispe.cafecampus.viewmodel.CafeViewModelFactory
import com.quispe.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val database = remember {
                CafeDatabase.obtenerInstancia(applicationContext)
            }

            val repository = remember {
                CafeRepositoryRoom(
                    usuarioDao = database.usuarioDao(),
                    productoDao = database.productoDao(),
                    pedidoDao = database.pedidoDao()
                )
            }

            val preferenciasStore = remember {
                PreferenciasStore(applicationContext)
            }

            val factory = remember {
                CafeViewModelFactory(repository, preferenciasStore)
            }

            val sesionViewModel: SesionViewModel = viewModel(
                factory = factory
            )

            val modoOscuro by sesionViewModel.modoOscuro.collectAsState()
            val usuario by sesionViewModel.usuarioActual.collectAsState()
            val perfilGuardado by sesionViewModel.perfilGuardado.collectAsState()

            val rol = when {
                usuario?.rol == Rol.ADMIN.name -> Rol.ADMIN
                usuario?.rol == Rol.CLIENTE.name -> Rol.CLIENTE
                perfilGuardado == Rol.ADMIN -> Rol.ADMIN
                else -> Rol.CLIENTE
            }

            CafeTheme(
                rol = rol,
                darkTheme = modoOscuro
            ) {
                NavCafe(
                    sesionViewModel = sesionViewModel,
                    repository = repository,
                    preferenciasStore = preferenciasStore
                )
            }
        }
    }
}
