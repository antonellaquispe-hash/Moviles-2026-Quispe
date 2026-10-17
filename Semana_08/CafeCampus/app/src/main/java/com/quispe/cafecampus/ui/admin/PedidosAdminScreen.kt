package com.quispe.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.cafecampus.data.local.relation.PedidoConDetalle
import com.quispe.cafecampus.viewmodel.PedidosAdminViewModel
import java.util.Locale

@Composable
fun PedidosAdminScreen(
    viewModel: PedidosAdminViewModel,
    onVerDetalle: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val recibidos by viewModel.pedidosRecibidos.collectAsState()
    val preparando by viewModel.pedidosPreparando.collectAsState()
    val listos by viewModel.pedidosListos.collectAsState()

    val pedidos = (recibidos + preparando + listos)
        .distinctBy { it.pedido.id }
        .sortedByDescending { it.pedido.fecha }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de pedidos",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text("Volver al panel")
            }

            if (pedidos.isEmpty()) {
                Text("No hay pedidos pendientes en este momento.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidos, key = { it.pedido.id }) { pedido ->
                        PedidoAdminCard(
                            pedido = pedido,
                            onVerDetalle = {
                                onVerDetalle(pedido.pedido.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoAdminCard(
    pedido: PedidoConDetalle,
    onVerDetalle: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Pedido #${pedido.pedido.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Estado: ${pedido.pedido.estado}")
            Text("Hora de recojo: ${pedido.pedido.horaRecojo}")
            Text("Pago: ${pedido.pedido.pago}")

            Text(
                text = String.format(
                    Locale("es", "PE"),
                    "Total: S/ %.2f",
                    pedido.pedido.total
                )
            )

            if (pedido.pedido.nota.isNotBlank()) {
                Text("Nota: ${pedido.pedido.nota}")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onVerDetalle,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver detalle y actualizar estado")
                }
            }
        }
    }
}