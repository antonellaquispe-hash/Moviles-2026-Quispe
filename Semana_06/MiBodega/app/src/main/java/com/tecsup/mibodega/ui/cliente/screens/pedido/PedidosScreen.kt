package com.tecsup.mibodega.ui.cliente.screens.pedidos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.componentes.BotonSecundario

@Composable
fun PedidosScreen(
    pedidos: List<Pedido>,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Mis pedidos",
            fontWeight = FontWeight.Bold
        )

        if (pedidos.isEmpty()) {

            Text(
                text = "Todavía no tienes pedidos realizados."
            )

            BotonSecundario(
                texto = "Volver",
                onClick = onVolver
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos) { pedido ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "Pedido #${pedido.id}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Tipo de entrega: ${pedido.tipoEntrega}"
                            )

                            pedido.productos.forEach { item ->

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${item.producto.nombre} x${item.cantidad}",
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "S/ %.2f".format(
                                            item.producto.precio * item.cantidad
                                        )
                                    )
                                }
                            }

                            Text(
                                text = "Entrega: S/ %.2f".format(
                                    pedido.costoEntrega
                                )
                            )

                            Text(
                                text = "Total: S/ %.2f".format(
                                    pedido.total
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
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