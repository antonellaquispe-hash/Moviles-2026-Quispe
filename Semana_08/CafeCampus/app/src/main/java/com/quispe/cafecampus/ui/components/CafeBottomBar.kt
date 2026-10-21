package com.quispe.cafecampus.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ItemNavegacion(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

/**
 * Barra de navegacion inferior. El cliente usa 3 items (Menu, Pedidos, Perfil)
 * y el administrador 4 (Panel, Pedidos, Menu, Ajustes).
 */
@Composable
fun CafeBottomBar(
    items: List<ItemNavegacion>,
    rutaActual: String,
    onNavegar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        items.forEach { item ->
            val seleccionado = item.ruta == rutaActual

            NavigationBarItem(
                selected = seleccionado,
                onClick = { onNavegar(item.ruta) },
                icon = {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.etiqueta
                    )
                },
                label = {
                    Text(
                        text = item.etiqueta,
                        fontWeight = if (seleccionado) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
