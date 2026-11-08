package com.tecsup.rockvault.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.components.Cargando

@Composable
fun AdminScreen(
    estado: UiState<ResumenAdmin>,
    mensaje: String?,
    onSincronizar: () -> Unit,
    onLimpiarCache: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> {
                Text(estado.mensaje, color = MaterialTheme.colorScheme.error)
                OutlinedButton(onClick = onReintentar) { Text("Reintentar") }
            }
            UiState.Vacio -> Text("No hay datos guardados")
            is UiState.Exito -> {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaTotal("Elementos guardados", estado.datos.totalGuardado, Modifier.weight(1f))
                    TarjetaTotal("Favoritos", estado.datos.totalFavoritos, Modifier.weight(1f))
                }
            }
        }
        if (mensaje != null) {
            Text(mensaje, color = MaterialTheme.colorScheme.secondary)
        }
        Button(onClick = onSincronizar, modifier = Modifier.fillMaxWidth()) {
            Text("Sincronizar")
        }
        OutlinedButton(
            onClick = onLimpiarCache,
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
        ) {
            Text("Limpiar caché")
        }
    }
}

@Composable
private fun TarjetaTotal(titulo: String, total: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(total.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}
