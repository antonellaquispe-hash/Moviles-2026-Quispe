
package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.model.EstadoPedido
import java.util.Locale

@Composable
fun DetallePedidoAdminScreen(
    pedido: PedidoConDetalle,
    onCambiarEstado: (EstadoPedido) -> Unit,
    onCancelar: () -> Unit,
    onVolver: () -> Unit
) {
    var mostrarConfirmacion by remember(pedido.pedido.id) {
        mutableStateOf(false)
    }

    val estadoActual = runCatching {
        EstadoPedido.valueOf(pedido.pedido.estado)
    }.getOrNull()

    val siguienteEstado = estadoActual?.siguiente()

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            title = {
                Text("Cancelar pedido")
            },
            text = {
                Text(
                    "¿Seguro que deseas cancelar el pedido #${pedido.pedido.id}? Esta acción cambiará su estado a Cancelado."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        onCancelar()
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Volver")
                }
            }
        )
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

                    Text(
                        "Estado: ${estadoActual?.etiqueta() ?: pedido.pedido.estado}"
                    )

                    Text("Hora de recojo: ${pedido.pedido.horaRecojo}")
                    Text("Método de pago: ${pedido.pedido.pago}")

                    Text(
                        text = String.format(
                            Locale("es", "PE"),
                            "Total: S/ %.2f",
                            pedido.pedido.total
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (pedido.pedido.nota.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.CardDefaults
                                .cardColors(
                                    containerColor = MaterialTheme.colorScheme
                                        .tertiaryContainer
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text("Nota del cliente")
                                Text(pedido.pedido.nota)
                            }
                        }
                    }
                }
            }

            Text(
                text = "Productos del pedido",
                style = MaterialTheme.typography.titleMedium
            )

            if (pedido.detalles.isEmpty()) {
                Text("No hay productos registrados en este pedido.")
            } else {
                pedido.detalles.forEach { detalle ->
                    Text(
                        "${detalle.cantidad} × ${detalle.nombreProducto} — " +
                                String.format(
                                    Locale("es", "PE"),
                                    "S/ %.2f",
                                    detalle.cantidad * detalle.precioUnitario
                                )
                    )
                }
            }

            Text(
                text = "Actualización del pedido",
                style = MaterialTheme.typography.titleMedium
            )

            if (siguienteEstado != null) {
                Button(
                    onClick = {
                        onCambiarEstado(siguienteEstado)
                    },
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
                Text(
                    text = when (estadoActual) {
                        EstadoPedido.ENTREGADO ->
                            "El pedido ya fue entregado."
                        EstadoPedido.CANCELADO ->
                            "Este pedido fue cancelado."
                        else ->
                            "No hay más transiciones disponibles."
                    }
                )
            }

            if (estadoActual?.puedeCancelarse() == true) {
                OutlinedButton(
                    onClick = {
                        mostrarConfirmacion = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
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