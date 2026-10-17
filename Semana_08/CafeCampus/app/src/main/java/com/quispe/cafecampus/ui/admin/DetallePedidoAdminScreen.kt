package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import java.util.Locale

@Composable
fun DetallePedidoAdminScreen(
    pedido: PedidoConDetalle,
    onCambiarEstado: (EstadoPedido) -> Unit,
    onVolver: () -> Unit
) {
    val siguienteEstado = when (pedido.pedido.estado) {
        EstadoPedido.RECIBIDO.name -> EstadoPedido.PREPARANDO
        EstadoPedido.PREPARANDO.name -> EstadoPedido.LISTO
        EstadoPedido.LISTO.name -> EstadoPedido.ENTREGADO
        else -> null
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Detalle del pedido #${pedido.pedido.id}",
                style = MaterialTheme.typography.headlineSmall
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Cliente: ${pedido.pedido.clienteId}")
                    Text("Estado: ${pedido.pedido.estado}")
                    Text("Hora de recojo: ${pedido.pedido.horaRecojo}")
                    Text("Método de pago: ${pedido.pedido.pago}")

                    Text(
                        String.format(
                            Locale("es", "PE"),
                            "Total: S/ %.2f",
                            pedido.pedido.total
                        )
                    )

                    if (pedido.pedido.nota.isNotBlank()) {
                        Text("Nota del cliente: ${pedido.pedido.nota}")
                    }
                }
            }

            Text(
                text = "Actualización del pedido",
                style = MaterialTheme.typography.titleMedium
            )

            if (siguienteEstado != null) {
                Button(
                    onClick = { onCambiarEstado(siguienteEstado) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when (siguienteEstado) {
                            EstadoPedido.PREPARANDO -> "Comenzar preparación"
                            EstadoPedido.LISTO -> "Marcar como listo"
                            EstadoPedido.ENTREGADO -> "Marcar como entregado"
                            else -> "Actualizar estado"
                        }
                    )
                }
            } else {
                Text("Este pedido ya no tiene más transiciones disponibles.")
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a pedidos")
            }
        }
    }
}