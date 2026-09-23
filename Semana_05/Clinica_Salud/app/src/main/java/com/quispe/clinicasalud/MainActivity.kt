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
            Inicio(
                onMedicoSeleccionado = { nombre, especialidad, valoracion ->
                    navController.navigate(
                        "perfil/${Uri.encode(nombre)}/${Uri.encode(especialidad)}/$valoracion"
                    )
                }
            )
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

            Perfil(
                nombre = nombre,
                especialidad = especialidad,
                valoracion = valoracion
            )
        }
    }
}