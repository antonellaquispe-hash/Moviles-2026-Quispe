package com.tecsup.rockvault.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun FavoritosScreen(
    estado: UiState<List<Item>>,
    onItemClick: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> ErrorView(
                mensaje = estado.mensaje,
                onReintentar = {},
                modifier = Modifier.weight(1f)
            )
            is UiState.Exito -> ListaDeFavoritos(
                items = estado.datos,
                onItemClick = onItemClick,
                onAlternarFavorito = onAlternarFavorito,
                modifier = Modifier.weight(1f)
            )
            UiState.Vacio -> Text(
                text = "Aún no tienes favoritos",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ListaDeFavoritos(
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
