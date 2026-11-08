package com.tecsup.rockvault.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.components.Cargando
import com.tecsup.rockvault.ui.components.ErrorView

@Composable
fun DetalleScreen(
    estado: UiState<Item>,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onVolver) {
            Text(text = "Volver")
        }

        when (estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> ErrorView(
                mensaje = estado.mensaje,
                onReintentar = onReintentar,
                modifier = Modifier.weight(1f)
            )
            is UiState.Exito -> DetalleItem(
                item = estado.datos,
                onAlternarFavorito = onAlternarFavorito,
                modifier = Modifier.weight(1f)
            )
            UiState.Vacio -> Text(text = "No hay detalle para mostrar")
        }
    }
}

@Composable
private fun DetalleItem(
    item: Item,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = item.titulo,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = item.subtitulo,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Identificador: ${item.id}",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedButton(onClick = onAlternarFavorito) {
            Text(text = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos")
        }
    }
}
