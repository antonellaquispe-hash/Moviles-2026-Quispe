package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val FAVORITOS = "favoritos"
    const val PEDIDOS = "pedidos"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    var carrito by remember {
        mutableStateOf<List<ItemCarrito>>(emptyList())
    }

    var favoritos by remember {
        mutableStateOf<List<Producto>>(emptyList())
    }

    var pedidos by remember {
        mutableStateOf<List<Pedido>>(emptyList())
    }

    var tipoEntrega by remember {
        mutableStateOf("Delivery")
    }

    var costoEntrega by remember {
        mutableStateOf(4.00)
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {

        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN)
                },
                onTerminos = {
                }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onIngresar = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onCrearCuenta = { _, _, _, _ ->
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf {
                    it.cantidad
                },
                onVerCarrito = {
                    navController.navigate(Rutas.CARRITO)
                },
                onVerFavoritos = {
                    navController.navigate(Rutas.FAVORITOS)
                },
                onVerPedidos = {
                    navController.navigate(Rutas.PEDIDOS)
                },
                onProductoClick = { producto ->
                    navController.navigate(
                        Rutas.detalle(producto.id)
                    )
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(
                        carrito = carrito,
                        producto = producto,
                        cantidad = 1
                    )
                }
            )
        }

        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                favoritos = favoritos,
                onVolver = {
                    navController.popBackStack()
                },
                onEliminarFavorito = { producto ->
                    favoritos = favoritos.filterNot {
                        it.id == producto.id
                    }
                },
                onAgregarCarrito = { producto ->
                    carrito = agregarOSumarProducto(
                        carrito = carrito,
                        producto = producto,
                        cantidad = 1
                    )
                },
                onProductoClick = { producto ->
                    navController.navigate(
                        Rutas.detalle(producto.id)
                    )
                }
            )
        }

        composable(Rutas.PEDIDOS) {
            PedidosScreen(
                pedidos = pedidos,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productoId =
                backStackEntry.arguments?.getInt("productoId") ?: 0

            val producto =
                listaProductosFake.first {
                    it.id == productoId
                }

            DetalleProductoScreen(
                producto = producto,
                esFavorito = favoritos.any {
                    it.id == producto.id
                },
                onCambiarFavorito = { productoSeleccionado ->

                    favoritos = if (
                        favoritos.any {
                            it.id == productoSeleccionado.id
                        }
                    ) {
                        favoritos.filterNot {
                            it.id == productoSeleccionado.id
                        }
                    } else {
                        favoritos + productoSeleccionado
                    }
                },
                onVolver = {
                    navController.popBackStack()
                },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->

                    carrito = agregarOSumarProducto(
                        carrito = carrito,
                        producto = productoSeleccionado,
                        cantidad = cantidad
                    )

                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = {
                    navController.popBackStack()
                },
                onIncrementar = { producto ->

                    carrito = carrito.map {
                        if (it.producto.id == producto.id) {
                            it.copy(
                                cantidad = it.cantidad + 1
                            )
                        } else {
                            it
                        }
                    }
                },
                onDecrementar = { producto ->

                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> {
                                it
                            }

                            it.cantidad > 1 -> {
                                it.copy(
                                    cantidad = it.cantidad - 1
                                )
                            }

                            else -> {
                                null
                            }
                        }
                    }
                },
                onEliminar = { producto ->

                    carrito = carrito.filterNot {
                        it.producto.id == producto.id
                    }
                },
                onContinuarPedido = {
                    navController.navigate(Rutas.ENTREGA)
                }
            )
        }

        composable(Rutas.ENTREGA) {
            DatosEntregaScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onContinuar = { tipo, costo ->

                    tipoEntrega = tipo
                    costoEntrega = costo

                    navController.navigate(
                        Rutas.CONFIRMACION
                    )
                }
            )
        }

        composable(Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                carrito = carrito,
                tipoEntrega = tipoEntrega,
                costoEntrega = costoEntrega,
                onVolver = {
                    navController.popBackStack()
                },
                onFinalizar = {

                    val subtotal = carrito.sumOf {
                        it.producto.precio * it.cantidad
                    }

                    val total = subtotal + costoEntrega

                    val nuevoPedido = Pedido(
                        id = pedidos.size + 1,
                        productos = carrito,
                        tipoEntrega = tipoEntrega,
                        costoEntrega = costoEntrega,
                        total = total
                    )

                    pedidos = pedidos + nuevoPedido

                    carrito = emptyList()

                    tipoEntrega = "Delivery"
                    costoEntrega = 4.00

                    navController.navigate(
                        Rutas.INICIO
                    ) {
                        popUpTo(Rutas.INICIO) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {

    val existe = carrito.any {
        it.producto.id == producto.id
    }

    return if (existe) {

        carrito.map {
            if (it.producto.id == producto.id) {
                it.copy(
                    cantidad = it.cantidad + cantidad
                )
            } else {
                it
            }
        }

    } else {

        carrito + ItemCarrito(
            producto = producto,
            cantidad = cantidad
        )
    }
}