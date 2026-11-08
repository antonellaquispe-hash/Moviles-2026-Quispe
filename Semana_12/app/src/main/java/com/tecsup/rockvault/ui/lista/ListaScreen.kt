package com.tecsup.rockvault.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Snackbar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.rockvault.domain.model.Item
import com.tecsup.rockvault.ui.UiState
import com.tecsup.rockvault.ui.components.Cargando
import com.tecsup.rockvault.ui.components.ErrorView
import com.tecsup.rockvault.ui.components.ItemCard

@Composable
fun ListaScreen(
    estado: UiState<List<Item>>,
    aviso: String?,
    onReintentar: () -> Unit,
    onItemClick: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(
                    Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))
                ).padding(22.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("ROCKVAULT  /  LECTURAS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary)
                    Text("Ideas que\nencienden", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary)
                    Text("Publicaciones para descubrir", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .86f))
                }
                Text("✦", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        if (aviso != null) {
            Snackbar(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = aviso)
            }
        }

        when (estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> ErrorView(
                mensaje = estado.mensaje,
                onReintentar = onReintentar,
                modifier = Modifier.weight(1f)
            )
            is UiState.Exito -> ListaDeItems(
                items = estado.datos,
                onItemClick = onItemClick,
                onAlternarFavorito = onAlternarFavorito,
                modifier = Modifier.weight(1f)
            )
            UiState.Vacio -> Text(
                text = "No hay elementos para mostrar",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ListaDeItems(
    items: List<Item>,
    onItemClick: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = Item::id) { item ->
            ItemCard(
                item = item,
                onClick = { onItemClick(item.id) },
                onAlternarFavorito = { onAlternarFavorito(item.id) }
            )
        }
    }
}
