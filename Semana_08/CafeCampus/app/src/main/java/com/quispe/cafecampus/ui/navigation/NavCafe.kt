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
import androidx.compose.runtime.LaunchedEffect
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
import com.quispe.cafecampus.data.prefs.PreferenciasStore
import com.quispe.cafecampus.data.repository.CafeRepository
import com.quispe.cafecampus.model.EstadoPedido
import com.quispe.cafecampus.model.Rol
import com.quispe.cafecampus.ui.admin.AjustesScreen
import com.quispe.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.quispe.cafecampus.ui.admin.MenuAdminScreen
import com.quispe.cafecampus.ui.admin.PanelScreen
import com.quispe.cafecampus.ui.admin.PedidosAdminScreen
import com.quispe.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.quispe.cafecampus.ui.cliente.DetalleProductoScreen
import com.quispe.cafecampus.ui.cliente.LoginScreen
import com.quispe.cafecampus.ui.cliente.MenuScreen
import com.quispe.cafecampus.ui.cliente.MisPedidosScreen
import com.quispe.cafecampus.viewmodel.CafeViewModelFactory
import com.quispe.cafecampus.viewmodel.MenuAdminViewModel
import com.quispe.cafecampus.viewmodel.MenuViewModel
import com.quispe.cafecampus.viewmodel.PanelAdminViewModel
import com.quispe.cafecampus.viewmodel.PedidosAdminViewModel
import com.quispe.cafecampus.viewmodel.PedidosClienteViewModel
import com.quispe.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    sesionViewModel: SesionViewModel,
    repository: CafeRepository,
    preferenciasStore: PreferenciasStore
) {
    val navController = rememberNavController()
    val usuario by sesionViewModel.usuarioActual.collectAsState()
    val factory = CafeViewModelFactory(repository, preferenciasStore)

    val menuViewModel: MenuViewModel = viewModel(factory = factory)
    val pedidosViewModel: PedidosClienteViewModel = viewModel(factory = factory)
    val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)
    val menuAdminViewModel: MenuAdminViewModel = viewModel(factory = factory)
    val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(factory = factory)

    val productos by menuViewModel.productos.collectAsState()
    val carrito by menuViewModel.carrito.collectAsState()
    val pedidoCreado by pedidosViewModel.pedidoCreado.collectAsState()
    val cargando by pedidosViewModel.cargando.collectAsState()
    val error by pedidosViewModel.error.collectAsState()

    LaunchedEffect(pedidoCreado) {
        val id = pedidoCreado
        if (id != null && id > 0L) {
            menuViewModel.vaciar()
            pedidosViewModel.limpiarPedidoCreado()
            navController.navigate(RutasCafe.seguimientoPedido(id.toInt()))
        }
    }

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
                        popUpTo(RutasCafe.LOGIN) { inclusive = true }
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
                    navController.navigate(RutasCafe.detalleProducto(productoId))
                },
                onCerrarSesion = {
                    menuViewModel.vaciar()
                    pedidosViewModel.limpiarError()
                    sesionViewModel.cerrarSesion()
                    navController.navigate(RutasCafe.LOGIN) {
                        popUpTo(RutasCafe.MENU) { inclusive = true }
                    }
                },
                onVerCarrito = {
                    navController.navigate(RutasCafe.CONFIRMAR_PEDIDO)
                },
                onVerMisPedidos = {
                    navController.navigate(RutasCafe.MIS_PEDIDOS)
                }
            )
        }

        composable(
            route = RutasCafe.DETALLE,
            arguments = listOf(navArgument("productoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId")
            val producto = productos.firstOrNull { it.id == productoId }

            if (producto != null) {
                DetalleProductoScreen(
                    producto = producto,
                    vm = menuViewModel,
                    onVolver = { navController.popBackStack() },
                    onAgregado = { navController.popBackStack() }
                )
            } else {
                PantallaMensaje(
                    titulo = "Producto no encontrado",
                    mensaje = "No se encontró el producto solicitado.",
                    textoBoton = "Volver al menú",
                    onClick = { navController.popBackStack() }
                )
            }
        }

        composable(RutasCafe.CONFIRMAR_PEDIDO) {
            ConfirmarPedidoScreen(
                items = carrito,
                cargando = cargando,
                error = error,
                onConfirmar = { horaRecojo, pago, nota ->
                    val clienteId = usuario?.id ?: 0
                    pedidosViewModel.confirmarPedido(
                        clienteId = clienteId,
                        items = carrito,
                        horaRecojo = horaRecojo,
                        pago = pago,
                        nota = nota
                    )
                },
                onVolver = {
                    pedidosViewModel.limpiarError()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = RutasCafe.SEGUIMIENTO,
            arguments = listOf(navArgument("pedidoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0

            LaunchedEffect(pedidoId) {
                pedidosViewModel.observarPedido(pedidoId)
            }

            val pedido by pedidosViewModel.pedidoActual.collectAsState()

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
                        text = "Pedido registrado",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text("Número de pedido: $pedidoId")
                    Text(
                        text = "Estado: ${pedido?.pedido?.estado ?: "Consultando..."}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        onClick = {
                            navController.navigate(RutasCafe.MENU) {
                                popUpTo(RutasCafe.MENU) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text("Volver al menú")
                    }
                    Button(
                        onClick = {
                            navController.navigate(RutasCafe.MIS_PEDIDOS)
                        }
                    ) {
                        Text("Ver mis pedidos")
                    }
                }
            }
        }

        composable(RutasCafe.MIS_PEDIDOS) {
            val clienteId = usuario?.id ?: 0

            LaunchedEffect(clienteId) {
                if (clienteId > 0) {
                    pedidosViewModel.cargarMisPedidos(clienteId)
                }
            }

            val misPedidos by pedidosViewModel.misPedidos.collectAsState()

            MisPedidosScreen(
                pedidos = misPedidos,
                cargando = cargando,
                onVerPedido = { pedidoId ->
                    navController.navigate(RutasCafe.seguimientoPedido(pedidoId))
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(RutasCafe.ADMIN) {
            PanelScreen(
                nombreAdmin = usuario?.nombre.orEmpty(),
                viewModel = panelAdminViewModel,
                onGestionarMenu = {
                    navController.navigate(RutasCafe.ADMIN_MENU)
                },
                onVerPedidos = {
                    navController.navigate(RutasCafe.ADMIN_PEDIDOS)
                },
                onAjustes = {
                    navController.navigate(RutasCafe.ADMIN_AJUSTES)
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(RutasCafe.LOGIN) {
                        popUpTo(RutasCafe.ADMIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(RutasCafe.ADMIN_MENU) {
            MenuAdminScreen(
                viewModel = menuAdminViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(RutasCafe.ADMIN_PEDIDOS) {
            PedidosAdminScreen(
                viewModel = pedidosAdminViewModel,
                onVerDetalle = { pedidoId ->
                    navController.navigate(RutasCafe.detallePedidoAdmin(pedidoId))
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = RutasCafe.ADMIN_DETALLE_PEDIDO,
            arguments = listOf(navArgument("pedidoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
            val pedidoFlow = androidx.compose.runtime.remember(pedidoId) {
                repository.observarPedido(pedidoId)
            }
            val pedido by pedidoFlow.collectAsState(initial = null)

            if (pedido != null) {
                DetallePedidoAdminScreen(
                    pedido = pedido!!,
                    onCambiarEstado = { estado ->
                        pedidosAdminViewModel.cambiarEstado(pedidoId, estado)
                    },
                    onVolver = { navController.popBackStack() }
                )
            } else {
                PantallaMensaje(
                    titulo = "Detalle del pedido",
                    mensaje = "Cargando pedido o el pedido ya no existe.",
                    textoBoton = "Volver a pedidos",
                    onClick = { navController.popBackStack() }
                )
            }
        }

        composable(RutasCafe.ADMIN_AJUSTES) {
            AjustesScreen(
                nombreAdmin = usuario?.nombre.orEmpty(),
                onVolver = { navController.popBackStack() },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(RutasCafe.LOGIN) {
                        popUpTo(RutasCafe.ADMIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun PantallaMensaje(
    titulo: String,
    mensaje: String,
    textoBoton: String,
    onClick: () -> Unit
) {
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
                text = titulo,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = mensaje,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Button(onClick = onClick) {
                Text(textoBoton)
            }
        }
    }
}