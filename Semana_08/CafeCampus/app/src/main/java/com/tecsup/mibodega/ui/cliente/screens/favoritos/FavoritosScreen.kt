package com.tecsup.mibodega.ui.cliente.screens.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario

@Composable
fun FavoritosScreen(
    favoritos: List<Producto>,
    onVolver: () -> Unit,
    onEliminarFavorito: (Producto) -> Unit,
    onAgregarCarrito: (Producto) -> Unit,
    onProductoClick: (Producto) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Mis favoritos",
            fontWeight = FontWeight.Bold
        )

        if (favoritos.isEmpty()) {

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null
            )

            Text(
                text = "No tienes productos favoritos todavía."
            )

            BotonSecundario(
                texto = "Volver",
                onClick = onVolver
            )

        } else {

            favoritos.forEach { producto ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onProductoClick(producto)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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

                                Text(
                                    text = "S/ %.2f".format(producto.precio)
                                )
                            }

                            IconButton(
                                onClick = {
                                    onEliminarFavorito(producto)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar favorito"
                                )
                            }
                        }

                        BotonPrimario(
                            texto = "Agregar al carrito",
                            onClick = {
                                onAgregarCarrito(producto)
                            }
                        )
                    }
                }
            }

            BotonSecundario(
                texto = "Volver",
                onClick = onVolver
            )
        }
    }
}