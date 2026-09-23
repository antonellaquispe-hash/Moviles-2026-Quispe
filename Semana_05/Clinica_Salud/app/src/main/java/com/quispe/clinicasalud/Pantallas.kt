package com.quispe.clinicasalud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Inicio(
    onMedicoSeleccionado: (String, String, String) -> Unit
) {
    val especialidades = listOf(
        "Cardiología",
        "Pediatría",
        "Dermatología",
        "Odontología"
    )

    val medicos = listOf(
        Triple("Dra. María López", "Cardiología", "4.9"),
        Triple("Dr. Carlos Pérez", "Pediatría", "4.8"),
        Triple("Dra. Ana Torres", "Dermatología", "4.7"),
        Triple("Dr. Luis Ramírez", "Odontología", "4.9")
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
                        .background(Color.Transparent)
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

        Text(
            text = "Médicos disponibles",
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(medicos) { medico ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onMedicoSeleccionado(
                                medico.first,
                                medico.second,
                                medico.third
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = medico.first
                        )

                        Text(
                            text = medico.second,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        Text(
                            text = "⭐ ${medico.third}",
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Perfil(
    nombre: String,
    especialidad: String,
    valoracion: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Perfil médico"
        )

        Text(
            text = nombre,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Especialidad: $especialidad",
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            text = "Valoración: ⭐ $valoracion",
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}