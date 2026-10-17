package com.quispe.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.Rol
import com.quispe.cafecampus.ui.cliente.DetalleProductoScreen
import com.quispe.cafecampus.ui.cliente.LoginScreen
import com.quispe.cafecampus.ui.cliente.MenuScreen
import com.quispe.cafecampus.viewmodel.CafeViewModelFactory
import com.quispe.cafecampus.viewmodel.MenuViewModel
import com.quispe.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel,
    repository: CafeRepository
) {
    val navController = rememberNavController()
    val usuario by sesionViewModel.usuarioActual.collectAsState()

    val menuViewModel: MenuViewModel = viewModel(
        factory = CafeViewModelFactory(repository)
    )

    val productos by menuViewModel.productos.collectAsState()

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
            MenuScreen(
                vm = menuViewModel,
                nombreUsuario = usuario?.nombre.orEmpty(),
                onProducto = { productoId ->
                    navController.navigate(
                        RutasCafe.detalleProducto(productoId)
                    )
                },
                onCerrarSesion = {
                    menuViewModel.vaciar()
                    sesionViewModel.cerrarSesion()
                    navController.navigate(RutasCafe.LOGIN) {
                        popUpTo(RutasCafe.MENU) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = RutasCafe.DETALLE,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments
                ?.getInt("productoId")

            val producto = productos.firstOrNull {
                it.id == productoId
            }

            if (producto != null) {
                DetalleProductoScreen(
                    producto = producto,
                    vm = menuViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onAgregado = {
                        navController.popBackStack()
                    }
                )
            } else {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se encontró el producto.")
                        Button(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Text("Volver al menú")
                        }
                    }
                }
            }
        }

        composable(RutasCafe.ADMIN) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Panel de administración",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Bienvenido, ${usuario?.nombre.orEmpty()}"
                    )
                    Button(
                        onClick = {
                            sesionViewModel.cerrarSesion()
                            navController.navigate(RutasCafe.LOGIN) {
                                popUpTo(RutasCafe.ADMIN) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}