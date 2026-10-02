package com.quispe.tecsupstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Producto(
    val nombre: String,
    val categoria: String,
    val precio: Double
)

@Composable
fun ProductoCard(producto: Producto) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "S/ ${producto.precio}",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            IconButton(
                onClick = {
                    expanded = !expanded
                }
            ) {

                Text(
                    text = "⋮",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}