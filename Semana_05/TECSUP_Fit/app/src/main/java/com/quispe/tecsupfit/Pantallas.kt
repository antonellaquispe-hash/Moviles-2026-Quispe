package com.quispe.tecsupfit

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Inicio(
    onDetalle: () -> Unit
) {
    val filtros = listOf(
        "Hoy",
        "Esta semana"
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

        Button(
            onClick = onDetalle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Ver clases")
        }
    }
}

@Composable
fun DetalleClase() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Detalle de clase")
    }
}