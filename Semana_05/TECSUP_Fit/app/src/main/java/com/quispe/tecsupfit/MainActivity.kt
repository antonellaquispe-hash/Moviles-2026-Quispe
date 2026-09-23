package com.quispe.tecsupfit

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
            Inicio(
                onClaseSeleccionada = { nombre, horario ->
                    navController.navigate(
                        "detalle/${Uri.encode(nombre)}/${Uri.encode(horario)}"
                    )
                }
            )
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
            Text("Reservas")
        }
    }
}