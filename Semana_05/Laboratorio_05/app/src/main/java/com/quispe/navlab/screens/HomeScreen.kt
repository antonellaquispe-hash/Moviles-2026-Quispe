package com.quispe.navlab.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.quispe.navlab.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Navegación Jetpack Compose",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                navController.navigate(Screen.List.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver lista de elementos")
        }

        Button(
            onClick = {
                navController.navigate(Screen.Profile.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Mi perfil")
        }
    }
}