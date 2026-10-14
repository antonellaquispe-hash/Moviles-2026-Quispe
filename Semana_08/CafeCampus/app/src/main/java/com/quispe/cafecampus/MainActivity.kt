
package com.quispe.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quispe.cafecampus.data.local.CafeDatabase
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepositoryRoom
import com.quispe.cafecampus.ui.navigation.NavCafe
import com.quispe.cafecampus.ui.theme.CafeCampusTheme
import com.quispe.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CafeCampusTheme {
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
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(
                            modelClass: Class<T>
                        ): T {
                            if (modelClass.isAssignableFrom(
                                    SesionViewModel::class.java
                                )
                            ) {
                                @Suppress("UNCHECKED_CAST")
                                return SesionViewModel(
                                    repository,
                                    preferenciasStore
                                ) as T
                            }

                            throw IllegalArgumentException(
                                "ViewModel desconocido: ${modelClass.name}"
                            )
                        }
                    }
                }

                val sesionViewModel: SesionViewModel = viewModel(
                    factory = factory
                )

                NavCafe(
                    sesionViewModel = sesionViewModel
                )
            }
        }
    }
}