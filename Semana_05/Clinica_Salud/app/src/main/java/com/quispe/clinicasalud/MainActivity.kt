package com.quispe.clinicasalud

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
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
            AppDrawer(
                destinoActual = "inicio",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                Inicio(
                    onMedicoSeleccionado = { nombre, especialidad, valoracion ->
                        navController.navigate(
                            "perfil/${Uri.encode(nombre)}/${Uri.encode(especialidad)}/$valoracion"
                        )
                    }
                )
            }
        }

        composable(
            route = "perfil/{nombre}/{especialidad}/{valoracion}"
        ) { backStackEntry ->

            val nombre = backStackEntry.arguments
                ?.getString("nombre")
                ?: ""

            val especialidad = backStackEntry.arguments
                ?.getString("especialidad")
                ?: ""

            val valoracion = backStackEntry.arguments
                ?.getString("valoracion")
                ?: ""

            AppDrawer(
                destinoActual = "inicio",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                Perfil(
                    nombre = nombre,
                    especialidad = especialidad,
                    valoracion = valoracion,
                    onAgendar = {
                        navController.navigate(
                            "agendar/${Uri.encode(nombre)}/${Uri.encode(especialidad)}"
                        )
                    }
                )
            }
        }

        composable(
            route = "agendar/{nombre}/{especialidad}"
        ) { backStackEntry ->

            val nombre = backStackEntry.arguments
                ?.getString("nombre")
                ?: ""

            val especialidad = backStackEntry.arguments
                ?.getString("especialidad")
                ?: ""

            AppDrawer(
                destinoActual = "inicio",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                AgendarCita(
                    nombre = nombre,
                    especialidad = especialidad,
                    onConfirmar = { fecha, horario ->
                        navController.navigate(
                            "confirmacion/${Uri.encode(nombre)}/${Uri.encode(especialidad)}/${Uri.encode(fecha)}/${Uri.encode(horario)}"
                        )
                    }
                )
            }
        }

        composable(
            route = "confirmacion/{nombre}/{especialidad}/{fecha}/{horario}"
        ) { backStackEntry ->

            val nombre = backStackEntry.arguments
                ?.getString("nombre")
                ?: ""

            val especialidad = backStackEntry.arguments
                ?.getString("especialidad")
                ?: ""

            val fecha = backStackEntry.arguments
                ?.getString("fecha")
                ?: ""

            val horario = backStackEntry.arguments
                ?.getString("horario")
                ?: ""

            AppDrawer(
                destinoActual = "inicio",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                Confirmacion(
                    nombre = nombre,
                    especialidad = especialidad,
                    fecha = fecha,
                    horario = horario,
                    onFinalizar = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        composable("citas") {
            AppDrawer(
                destinoActual = "citas",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                MisCitas()
            }
        }

        composable("historial") {
            AppDrawer(
                destinoActual = "historial",
                onNavegar = { destino ->
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            inclusive = true
                        }
                    }
                }
            ) {
                HistorialMedico()
            }
        }
    }
}