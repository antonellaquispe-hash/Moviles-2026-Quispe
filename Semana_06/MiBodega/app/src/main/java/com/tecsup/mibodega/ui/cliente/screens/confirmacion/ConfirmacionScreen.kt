package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario

@Composable
fun ConfirmacionScreen(
    carrito: List<ItemCarrito>,
    tipoEntrega: String,
    costoEntrega: Double,
    onVolver: () -> Unit,
    onFinalizar: () -> Unit
) {
    val subtotal = carrito.sumOf {
        it.producto.precio * it.cantidad
    }

    val total = subtotal + costoEntrega

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pedido confirmado",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Revisa el resumen de tu pedido antes de finalizar."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Resumen del pedido",
                    fontWeight = FontWeight.Bold
                )

                carrito.forEach { item ->
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
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Información de entrega",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Tipo: $tipoEntrega"
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Subtotal",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "S/ %.2f".format(subtotal)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Entrega",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "S/ %.2f".format(costoEntrega)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Total",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "S/ %.2f".format(total),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        BotonPrimario(
            texto = "Finalizar pedido",
            onClick = onFinalizar
        )

        BotonSecundario(
            texto = "Volver",
            onClick = onVolver
        )
    }
}