package com.quispe.tecsupfit

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            PantallaConBottomBar(
                destinoActual = "inicio",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Inicio(
                    onClaseSeleccionada = { nombre, horario ->
                        navController.navigate(
                            "detalle/${Uri.encode(nombre)}/${Uri.encode(horario)}"
                        )
                    }
                )
            }
        }

        composable(
            route = "detalle/{nombre}/{horario}"
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments
                ?.getString("nombre")
                ?: ""

            val horario = backStackEntry.arguments
                ?.getString("horario")
                ?: ""

            DetalleClase(
                nombre = nombre,
                horario = horario,
                onReservar = {
                    navController.navigate(
                        "confirmacion/${Uri.encode(nombre)}/${Uri.encode(horario)}"
                    )
                }
            )
        }

        composable(
            route = "confirmacion/{nombre}/{horario}"
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments
                ?.getString("nombre")
                ?: ""

            val horario = backStackEntry.arguments
                ?.getString("horario")
                ?: ""

            Confirmacion(
                nombre = nombre,
                horario = horario,
                onVerReservas = {
                    navController.navigate("reservas")
                }
            )
        }

        composable("reservas") {
            PantallaConBottomBar(
                destinoActual = "reservas",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Reservas()
            }
        }

        composable("rutinas") {
            PantallaConBottomBar(
                destinoActual = "rutinas",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Rutinas()
            }
        }

        composable("perfil") {
            PantallaConBottomBar(
                destinoActual = "perfil",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                PerfilUsuario()
            }
        }
    }
}

@Composable
fun PantallaConBottomBar(
    destinoActual: String,
    onNavegar: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BarraNavegacion(
                destinoActual = destinoActual,
                onNavegar = onNavegar
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            contenido()
        }
    }
}