
package com.quispe.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quispe.cafecampus.model.Rol
import com.quispe.cafecampus.ui.cliente.LoginScreen
import com.quispe.cafecampus.viewmodel.SesionViewModel

object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val ADMIN = "admin"
}

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel
) {
    val navController = rememberNavController()
    val usuario by sesionViewModel.usuarioActual.collectAsState()

    NavHost(
        navController = navController,
        startDestination = RutasCafe.LOGIN
    ) {
        composable(RutasCafe.LOGIN) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginExitoso = { rol ->
                    val destino = if (rol == Rol.ADMIN) {
                        RutasCafe.ADMIN
                    } else {
                        RutasCafe.MENU
                    }

                    navController.navigate(destino) {
                        popUpTo(RutasCafe.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(RutasCafe.MENU) {
            androidx.compose.material3.Surface {
                androidx.compose.foundation.layout.Column {
                    androidx.compose.material3.Text(
                        text = "CafeCampus"
                    )
                    androidx.compose.material3.Text(
                        text = "Bienvenido, ${usuario?.nombre.orEmpty()}"
                    )
                    androidx.compose.material3.Button(
                        onClick = {
                            sesionViewModel.cerrarSesion()
                            navController.navigate(RutasCafe.LOGIN) {
                                popUpTo(RutasCafe.MENU) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        androidx.compose.material3.Text("Cerrar sesión")
                    }
                }
            }
        }

        composable(RutasCafe.ADMIN) {
            androidx.compose.material3.Surface {
                androidx.compose.foundation.layout.Column {
                    androidx.compose.material3.Text(
                        text = "Panel de administración"
                    )
                    androidx.compose.material3.Text(
                        text = "Bienvenido, ${usuario?.nombre.orEmpty()}"
                    )
                    androidx.compose.material3.Button(
                        onClick = {
                            sesionViewModel.cerrarSesion()
                            navController.navigate(RutasCafe.LOGIN) {
                                popUpTo(RutasCafe.ADMIN) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        androidx.compose.material3.Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}