package com.quispe.clinicasalud

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Inicio(
    onPerfil: () -> Unit
) {
    val especialidades = listOf(
        "Cardiología",
        "Pediatría",
        "Dermatología",
        "Odontología"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Clínica Salud+"
        )

        Text(
            text = "Especialidades",
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(especialidades) { especialidad ->
                Card(
                    modifier = Modifier
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                        .padding(2.dp)
                ) {
                    Text(
                        text = especialidad,
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        )
                    )
                }
            }
        }

        Button(
            onClick = onPerfil,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Perfil médico")
        }
    }
}

@Composable
fun Perfil() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Perfil médico")
    }
}