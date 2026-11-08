package com.tecsup.rockvault.ui.detalle

import android.text.Html
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
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
        OutlinedButton(onClick = onVolver, shape = RoundedCornerShape(16.dp)) {
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
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(32.dp))
                .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))),
            contentAlignment = Alignment.Center
        ) {
            if (!item.imagenUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = "Imagen de ${item.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("¶", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        Text(
            text = item.titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "LECTURA  •  DEV COMMUNITY",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Text(
                text = Html.fromHtml(item.subtitulo, Html.FROM_HTML_MODE_COMPACT).toString(),
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        OutlinedButton(onClick = onAlternarFavorito) {
            Text(text = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos")
        }
    }
}
