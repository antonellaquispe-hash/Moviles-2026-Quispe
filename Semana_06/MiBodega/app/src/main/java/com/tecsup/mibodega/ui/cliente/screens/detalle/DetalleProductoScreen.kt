package com.tecsup.mibodega.ui.cliente.screens.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad

@Composable
fun DetalleProductoScreen(
    producto: Producto,
    esFavorito: Boolean,
    onCambiarFavorito: (Producto) -> Unit,
    onVolver: () -> Unit,
    onAgregarAlCarrito: (Producto, Int) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = producto.categoria
                )
            }

            IconButton(
                onClick = {
                    onCambiarFavorito(producto)
                }
            ) {
                Icon(
                    imageVector = if (esFavorito) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Favorito",
                    tint = if (esFavorito) Color.Red else Color.Gray
                )
            }
        }

        Text(
            text = producto.descripcion
        )

        Text(
            text = "S/ %.2f".format(producto.precio),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Cantidad",
            fontWeight = FontWeight.Bold
        )

        SelectorCantidad(
            cantidad = cantidad,
            onIncrementar = {
                cantidad++
            },
            onDecrementar = {
                if (cantidad > 1) {
                    cantidad--
                }
            }
        )

        BotonPrimario(
            texto = "Agregar al carrito",
            onClick = {
                onAgregarAlCarrito(producto, cantidad)
            }
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )
    }
}