package com.quispe.tecsupfit

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Inicio(
    onClaseSeleccionada: (String, String) -> Unit
) {
    val filtros = listOf(
        "Hoy",
        "Esta semana"
    )

    val clases = listOf(
        "Entrenamiento funcional" to "08:00",
        "Spinning" to "10:00",
        "Yoga" to "18:00",
        "Cross training" to "19:30"
    )

    var filtroSeleccionado by remember {
        mutableStateOf("Hoy")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TECSUP Fit"
        )

        Text(
            text = "Filtrar clases",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(filtros) { filtro ->
                Card(
                    modifier = Modifier.clickable {
                        filtroSeleccionado = filtro
                    }
                ) {
                    Text(
                        text = filtro,
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        )
                    )
                }
            }
        }

        Text(
            text = "Filtro seleccionado: $filtroSeleccionado",
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "Clases disponibles",
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clases) { clase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClaseSeleccionada(
                                clase.first,
                                clase.second
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = clase.first
                        )

                        Text(
                            text = "Horario: ${clase.second}",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleClase(
    nombre: String,
    horario: String,
    onReservar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de clase"
        )

        Text(
            text = nombre,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Horario: $horario",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onReservar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Reservar cupo")
        }
    }
}

@Composable
fun Confirmacion(
    nombre: String,
    horario: String,
    onVerReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reserva confirmada"
        )

        Text(
            text = "Clase: $nombre",
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Horario: $horario",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onVerReservas,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Ver reservas")
        }
    }
}